package fr.cleia.sia.domain.description.models;

import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.NodeType;
import fr.cleia.sia.domain.vo.Title;

public final class Piece extends EntiteArchivistique {
    public Piece(NodeId identifiant, NodeId identifiantParent, Depth profondeur, Title intitule) {
        super(identifiant, intitule, profondeur, identifiantParent, NodeType.PIECE);
        if (profondeur.value() < 1) {
            throw new IllegalArgumentException("La profondeur d'une piece piece doit étre >=1");
        }
    }

    @Override
    public EntiteArchivistique renommer(Title nouveauTitre) {
        return new Piece(identifiant(), parentId(), getProfondeur(), nouveauTitre);
    }

    @Override
    public EntiteArchivistique deplacer(NodeId nouveauParentId, Depth nouvelleProfondeur) {
        if (nouveauParentId == null) {
            throw new IllegalArgumentException("Le parent d'une Pièce ne peut pas être null");
        }
        if (nouvelleProfondeur.value() < 1) {
            throw new IllegalArgumentException("Profondeur invalide pour une Pièce");
        }
        return new Piece(identifiant(), nouveauParentId, nouvelleProfondeur, getIntitule());
    }

    public static Piece sous(NodeId parentId, Depth parentDepth, Title titre, NodeId newId){
        return new Piece(newId, parentId, parentDepth.increment(), titre);
    }

}
