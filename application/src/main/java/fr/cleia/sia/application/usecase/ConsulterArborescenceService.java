package fr.cleia.sia.application.usecase;

import fr.cleia.sia.domain.ports.DepotDeFonds;

public class ConsulterArborescenceService implements ConsulterArborescence{
    private final DepotDeFonds depotDeFonds;
    public ConsulterArborescenceService(DepotDeFonds depotDeFonds) {
        this.depotDeFonds = depotDeFonds;
    }

    @Override
    public Resultat executer(String identifiantFonds){
        var f = depotDeFonds.findById(identifiantFonds)
                .orElseThrow(() -> new IllegalArgumentException("Fonds introuvable: " + identifiantFonds));
        return new Resultat(
            f.identifiant(),
            f.intitule(),
            f.dossiers().stream().map(d->
                new Resultat.DossierR(
                        d.identifiant(),
                        d.intitule(),
                        d.cote(),
                        d.pieces().stream().map(p->
                                new Resultat.PieceR(
                                        p.identifiant(),
                                        p.intitule()
                                )
                        ).toList()
                )
            ).toList()
        );
    }
}
