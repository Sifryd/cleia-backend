package fr.cleia.sia.domain.description.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dossier extends EntiteArchivistique {
    private final String cote;
    private final List<Piece> pieces = new ArrayList<>();

    public Dossier(String identifiant, String intitule, String cote) {
        super(identifiant, intitule);
        this.cote = cote;
    }

    public void ajouterPiece(Piece piece) {
        pieces.add(piece);
    }

    public List<Piece> pieces() {
        return Collections.unmodifiableList(pieces);
    }
    public String cote() {return cote;}

    public String getCote() { return cote(); }
    public List<Piece> getPieces() { return pieces(); }
}
