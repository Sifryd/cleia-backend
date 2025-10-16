package fr.cleia.sia.domain.description.models;

import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.NodeType;
import fr.cleia.sia.domain.vo.Title;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Fonds extends EntiteArchivistique {

    private final List<Dossier> dossiers = new ArrayList<>();

    public Fonds(NodeId identifiant, Title intitule) {
        super(identifiant, intitule, new Depth(0), null, NodeType.FONDS);
    }

    @Override
    public EntiteArchivistique renommer(Title nouveauTitre) {
        return new Fonds(identifiant(), nouveauTitre);
    }

    @Override
    public EntiteArchivistique deplacer(NodeId nouveauParentId, Depth newDepth) {
        throw new IllegalStateException("Impossible de dépalacer un fonds");
    }

    public void ajouterDossier(Dossier dossier) {
        dossiers.add(dossier);
    }

    public List<Dossier> dossiers() {
        return Collections.unmodifiableList(dossiers);
    }

    // alias JavaBeans pour MapStruct
    public List<Dossier> getDossiers() { return dossiers(); }
}
