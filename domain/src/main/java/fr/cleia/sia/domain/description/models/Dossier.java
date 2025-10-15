package fr.cleia.sia.domain.description.models;

import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.NodeType;
import fr.cleia.sia.domain.vo.Title;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Dossier extends EntiteArchivistique {
    private final List<Piece> pieces = new ArrayList<>();

    public Dossier(NodeId identifiant, NodeId identifiantParent,  Depth profondeur, Title intitule) {
        super(identifiant, intitule, profondeur, identifiantParent, NodeType.DOSSIER);
        if (profondeur.value() < 1) {
            throw new IllegalArgumentException("la profondeur d'un dossier doit étre >= 1");
        }
    }

    @Override
    public EntiteArchivistique renommer(Title nouveauTitre) {
        return new Dossier(identifiant(), parentId(), getProfondeur(), nouveauTitre);
    }

    @Override
    public EntiteArchivistique deplacer(NodeId nouveauParentId, Depth nouvelleProfondeur) {
        if (nouveauParentId == null) {
            throw new IllegalArgumentException("Le parent d'un Dossier ne peut pas être null");
        }
        if (nouvelleProfondeur.value() < 1) {
            throw new IllegalArgumentException("Profondeur invalide pour un Dossier");
        }
        return new Dossier(identifiant(), nouveauParentId, nouvelleProfondeur, getIntitule());
    }


    public void ajouterPiece(Piece piece) {
        pieces.add(piece);
    }

    public List<Piece> pieces() {
        return Collections.unmodifiableList(pieces);
    }

    public List<Piece> getPieces() { return pieces(); }

    public static Dossier sous(NodeId parentId, Depth parentDepth, Title titre, NodeId newId){
        return new Dossier(newId, parentId, parentDepth.increment(), titre);
    }
}
