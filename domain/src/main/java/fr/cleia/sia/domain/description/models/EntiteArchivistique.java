package fr.cleia.sia.domain.description.models;

import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.NodeType;
import fr.cleia.sia.domain.vo.Title;

import java.util.Objects;

public sealed abstract class EntiteArchivistique permits Fonds, Dossier, Piece {
    private final NodeId identifiant;
    private final Title intitule;
    private final Depth profondeur;
    private final NodeId parentId;
    private final NodeType type;

    protected EntiteArchivistique(NodeId identifiant,
                                  Title intitule,
                                  Depth profondeur,
                                  NodeId parentId,
                                  NodeType type) {
        this.identifiant = Objects.requireNonNull(identifiant, "identifiant must not be null");
        this.intitule = Objects.requireNonNull(intitule, "intitule must not be null");
        this.profondeur = Objects.requireNonNull(profondeur, "profondeur must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");

        if (type == NodeType.FONDS) {
            if (parentId != null) {
                throw new IllegalArgumentException("parentId must be null for fonds entities");
            }
            if (profondeur.value() != 0) {
                throw new IllegalArgumentException("profondeur must be 0 for fonds entities");
            }
        }
        else {
            if (parentId == null) {
                throw new IllegalArgumentException("parentId must not be null for dossier and piece entities");
            }
            if (profondeur.value() < 1) {
                throw new IllegalArgumentException("profondeur must be positive for dossier and piece entities");
            }
        }
        this.parentId = parentId;
    }




    public NodeId identifiant() {return identifiant;}
    public Title getIntitule() { return intitule;}
    public Depth getProfondeur() { return profondeur;}
    public NodeId parentId() { return parentId;}
    public NodeType getType() { return type;}

    public abstract EntiteArchivistique renommer(Title nouveauTitre);
    public abstract EntiteArchivistique deplacer(NodeId nouveauParentId, Depth newDepth);

    protected final NodeId id() {return identifiant;}
    protected final Depth depth() {return profondeur;}
    protected final Title titre() {return intitule;}
    protected final NodeId parent() {return parentId;}
    protected final NodeType type() {return type;}

    @Override
    public String toString() {
        return "%s{id=%s, titre=%s, depth=%d, parent=%s}"
                .formatted(type, identifiant, intitule, profondeur.value(), parentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntiteArchivistique that)) return false;
        return identifiant.equals(that.identifiant);
    }

    @Override
    public int hashCode() {
        return identifiant.hashCode();
    }

}