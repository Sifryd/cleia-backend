package fr.cleia.sia.domain.description.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fonds extends EntiteArchivistique {

    private final List<Dossier> dossiers = new ArrayList<>();

    public Fonds(String identifiant, String intitule) {
        super(identifiant, intitule);
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
