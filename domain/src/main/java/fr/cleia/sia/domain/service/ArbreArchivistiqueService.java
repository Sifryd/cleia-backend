package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.exception.DomainError;
import fr.cleia.sia.domain.description.exception.DomainException;
import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.EntiteArchivistique;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.*;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.NodeType;
import fr.cleia.sia.domain.vo.Title;

import java.time.Period;
import java.util.Map;

public class ArbreArchivistiqueService {
    private final FinderDeNoeudArchivisitique finder;
    private final ArchiveNodeRepository archiveNodeRepository;

    public ArbreArchivistiqueService(
            FinderDeNoeudArchivisitique finder,
            ArchiveNodeRepository archiveNodeRepository
            ) {
        this.finder = finder;
        this.archiveNodeRepository = archiveNodeRepository;
    }

    public Fonds creerFonds(NodeId identifiant, Title titre) {
        var f = new Fonds(identifiant, titre);
        archiveNodeRepository.save(f);
        return f;
    }

    public Dossier creerDossierSous(NodeId parentId, NodeId id, Title titre) {
        var parent = charger(parentId);
        if (!peutAccepterEnfant(parent, NodeType.DOSSIER)){
            throw new DomainException(
                    DomainError.PARENT_REFUSE_DOSSIER,
                    "Parent refuse dossier",
                    Map.of("parentId", parentId.toString())
            );
        }
        var depth = parent.getProfondeur().increment();

        var d = new Dossier(id, parentId, depth, titre);
        archiveNodeRepository.save(d);
        return d;
    }

    public Piece creerPieceSous(NodeId parentId, NodeId id, Title titre) {
        var parent = charger(parentId);
        if (!peutAccepterEnfant(parent, NodeType.PIECE)){
            throw new DomainException(
                    DomainError.PARENT_REFUSE_PIECE,
                    "Parent refuse piece",
                    Map.of("parentId", parentId.toString())
            );
        }

        var depth = parent.getProfondeur().increment();

        var p = new Piece(id, parentId, depth, titre);
        archiveNodeRepository.save(p);
        return p;
    }

    public EntiteArchivistique renommer(NodeId id, Title titre) {
        var n = charger(id);
        var renomme = n.renommer(titre);
        archiveNodeRepository.save(renomme);
        return renomme;
    }

    public void deplacerSousArbre(NodeId noeudId, NodeId nouveauParentId){
        var noeud = charger(noeudId);
        if(noeud instanceof Fonds){
            throw new DomainException(
                    DomainError.NODE_NOT_FOUND,
                    "Noeud non trouvé",
                    Map.of("nodeId", noeudId.toString())
            );
        }
        if (noeudId.equals(nouveauParentId)) {
            throw new DomainException(
                    DomainError.MOVE_SELF_FORBIDDEN,
                    "Impossible de déplacer un noeud sous lui-même",
                    Map.of(
                            "nodeId", noeudId.toString(),
                            "newParentId", nouveauParentId.toString()
                    )
            );
        }
        var nouveauParent = charger(nouveauParentId);
        if (!peutAccepterEnfant(nouveauParent, noeud.getType())) {
            var err = (noeud.getType() == NodeType.DOSSIER)
                    ? DomainError.PARENT_REFUSE_DOSSIER
                    : DomainError.PARENT_REFUSE_PIECE;
            throw new DomainException(
                    err,
                    "Le nouveau parent ne peut pas accepter ce type d'enfant",
                    Map.of(
                            "nodeId", noeudId.toString(),
                            "newParentId", nouveauParentId.toString(),
                            "childType", noeud.getType().name()
                    )
            );
        }

        var subtree = finder.sousArbre(noeudId, -1).toList();

        var descendantIds = subtree.stream().map(EntiteArchivistique::identifiant).toList();
        if (descendantIds.contains(nouveauParentId)) {
            throw new DomainException(
                    DomainError.MOVE_DESC_FORBIDDEN,
                    "Impossible de déplacer un noeud sous l'un de ses descendants",
                    Map.of(
                            "nodeId", noeudId.toString(),
                            "newParentId", nouveauParentId.toString()
                    )
            );
        }

        int delta = (nouveauParent.getProfondeur().value() + 1) - noeud.getProfondeur().value();

        var movedNodes = subtree.stream()
                .map(n -> {
                    var newDepth = n.getProfondeur().plus(delta);
                    var newParent = n.identifiant().equals(noeudId) ? nouveauParentId : n.parentId();
                    return n.deplacer(newParent, newDepth);
                })
                .toList();

        archiveNodeRepository.saveAll(movedNodes);
    }

    public void supprimer(NodeId id) {
        boolean aDesEnfants = finder.sousArbre(id, 1).anyMatch(n -> !n.identifiant().equals(id));
        if (aDesEnfants) {
            throw new DomainException(
                    DomainError.DELETE_HAS_CHILDREN,
                    "Noeud a des enfants",
                    Map.of("nodeId", id.toString())
            );
        }
        archiveNodeRepository.deleteById(id);
    }

    // Helpers

    private EntiteArchivistique charger(NodeId id) {
        return archiveNodeRepository.findById(id)
                .map(e -> (EntiteArchivistique) e)
                .orElseThrow(() -> new DomainException(
                        DomainError.NODE_NOT_FOUND,
                        "Noeud non trouvé",
                        Map.of("nodeId", id.toString())
                ));
    }

    private static NodeType type(EntiteArchivistique n) {
        if (n instanceof Fonds) return NodeType.FONDS;
        if (n instanceof Dossier) return NodeType.DOSSIER;
        return NodeType.PIECE;
    }
    private static boolean peutAccepterEnfant(EntiteArchivistique parent, NodeType enfant) {
        return switch (type(parent)) {
            case FONDS -> enfant == NodeType.DOSSIER;
            case DOSSIER -> (enfant == NodeType.DOSSIER || enfant == NodeType.PIECE);
            case PIECE -> false;
        };
    }
}
