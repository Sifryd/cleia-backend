package fr.cleia.sia.application.usecase;

import java.util.List;

public interface ConsulterArborescence {
    record Resultat(String identifiantFonds, String intitule, List<DossierR> dossiers){
        public record DossierR(String identifiant, String intitule, String cote, List<PieceR> pieces){}
        public record PieceR(String identifiant, String intitule){}
    }
    Resultat executer (String identifiantFonds);
}
