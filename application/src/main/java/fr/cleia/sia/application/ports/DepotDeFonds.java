package fr.cleia.sia.application.ports;

import fr.cleia.sia.domain.description.models.Fonds;

public interface DepotDeFonds {
    Fonds sauvegarderFonds(Fonds fonds);
}
