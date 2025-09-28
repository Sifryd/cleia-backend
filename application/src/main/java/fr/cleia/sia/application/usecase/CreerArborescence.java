package fr.cleia.sia.application.usecase;

import java.util.List;

public interface CreerArborescence {
    record Commande(String identifiantFond, String intituleFonds, List<DossierCommande> dossiers){
        public record DossierCommande(String identifiantDossier, String intituleDossier, String cote, List<PieceCommande> pieces){}
        public record PieceCommande(String identifiantPiece, String intitulePiece){}
        }
    record Resultat(String identifiantFond){}
    Resultat executer(Commande commande);
}
