package fr.cleia.sia.application.usecase;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.ports.DepotDeFonds;
import fr.cleia.sia.domain.ports.FinderDeNoeudArchivisitique;
import fr.cleia.sia.domain.vo.NodeId;

public class ConsulterArborescenceService implements ConsulterArborescence{
    private final ArchiveNodeRepository archiveNodeRepository;
    private final FinderDeNoeudArchivisitique finder;

    public ConsulterArborescenceService(ArchiveNodeRepository archiveNodeRepository, FinderDeNoeudArchivisitique finder) {
        this.archiveNodeRepository = archiveNodeRepository;
        this.finder = finder;
    }

    @Override
    public Resultat executer(String identifiantFonds){
        var fondsId = new NodeId(java.util.UUID.fromString(identifiantFonds));
        var fonds = archiveNodeRepository.findById(fondsId)
                .orElseThrow(() -> new IllegalArgumentException("Fonds introuvable " + identifiantFonds));

        var children = finder.sousArbre(fondsId, 1).toList();
        var dossiers = children.stream()
                .filter(n -> n instanceof Dossier)
                .map(n -> (Dossier) n)
                .map(d -> new Resultat.DossierR(
                        d.identifiant().toString(),
                        d.getIntitule().value(),
                        null,
                        children.stream()
                                .filter(p -> p instanceof Piece && p.parentId().equals(d.identifiant()))
                        .map(p -> (Piece) p)
                        .map(p -> new Resultat.PieceR(
                                p.identifiant().toString(),
                                p.getIntitule().value()
                        ))
                        .toList()
                ));

        return new Resultat(
                fonds.identifiant().toString(),
                fonds.getIntitule().value(),
                dossiers.toList()
        );
    }
}
