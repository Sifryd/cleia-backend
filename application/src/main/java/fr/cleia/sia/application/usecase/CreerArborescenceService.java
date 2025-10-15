package fr.cleia.sia.application.usecase;

import fr.cleia.sia.domain.ports.DepotDeFonds;
import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.description.rules.PolitiqueDeDescription;

public class CreerArborescenceService implements CreerArborescence {
    private final DepotDeFonds depotDeFonds;
    private final PolitiqueDeDescription politique;

    public CreerArborescenceService(DepotDeFonds depotDeFonds, PolitiqueDeDescription politique) {
        this.depotDeFonds = depotDeFonds;
        this.politique = politique;
    }

    @Override
    public Resultat executer(Commande commande) {
        var fonds = new Fonds(commande.identifiantFond(), commande.intituleFonds());
        if (commande.dossiers() != null) {
            for (var dossierCommande : commande.dossiers()) {
                var dossier = new Dossier(
                        dossierCommande.identifiantDossier(),
                        dossierCommande.intituleDossier(),
                        dossierCommande.cote());
                if (dossierCommande.pieces() != null){
                    for (var pieceCommande : dossierCommande.pieces()) {
                        dossier.ajouterPiece(
                                new Piece(
                                    pieceCommande.identifiantPiece(),
                                    pieceCommande.intitulePiece()
                                )
                        );
                    }
                }
                fonds.ajouterDossier(dossier);
            }
        }

        politique.verifierArborescenceComplete(fonds);
        politique.verifierUniciteCotes(fonds);

        depotDeFonds.sauvegarderFonds(fonds);

        return new Resultat(fonds.identifiant());

    }
}
