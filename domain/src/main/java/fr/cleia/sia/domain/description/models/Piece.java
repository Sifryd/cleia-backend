package fr.cleia.sia.domain.description.models;

import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.NodeType;
import fr.cleia.sia.domain.vo.Title;

public final class Piece extends EntiteArchivistique {
    public Piece(NodeId identifiant, NodeId parentId, Depth profondeur, Title intitule) {
        super(identifiant, intitule, profondeur, parentId, NodeType.PIECE);
        if (profondeur.value() < 1) {
            throw new IllegalArgumentException("La profondeur d'une piece piece doit étre >=1");
        }
    }

    @Override
    public EntiteArchivistique renommer(Title nouveauTitre) {
        return new Piece(id(), parent(), depth(), nouveauTitre);
    }

    @Override
    public EntiteArchivistique deplacer(NodeId nouveauParentId, Depth nouvelleProfondeur) {
        if (nouveauParentId == null) {
            throw new IllegalArgumentException("Le parent d'une Pièce ne peut pas être null");
        }
        if (nouvelleProfondeur.value() < 1) {
            throw new IllegalArgumentException("Profondeur invalide pour une Pièce");
        }
        return new Piece(id(), nouveauParentId, nouvelleProfondeur, titre());
    }

    public static Piece sous(NodeId parentId, Depth parentDepth, Title titre, NodeId newId){
        return new Piece(newId, parentId, parentDepth.increment(), titre);
    }

}
