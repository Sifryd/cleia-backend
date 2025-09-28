package fr.cleia.sia.domain.description.models;

public abstract class EntiteArchivistique {
    private final String identifiant;
    private final String intitule;

    protected EntiteArchivistique(String identifiant, String intitule) {
        if (identifiant == null || identifiant.isBlank()) {
            throw new IllegalArgumentException("L'identifiant est obligatoire");
        }
        if (intitule == null || intitule.isBlank()) {
            throw new IllegalArgumentException("L'intitule est obligatoire");
        }
        this.identifiant = identifiant;
        this.intitule = intitule;
    }

    public String identifiant() {return identifiant;}
    public String intitule() { return intitule;}


    public String getIdentifiant() { return identifiant(); }
    public String getIntitule() { return intitule(); }
}