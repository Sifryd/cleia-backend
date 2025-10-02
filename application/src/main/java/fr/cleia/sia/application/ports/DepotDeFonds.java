package fr.cleia.sia.application.ports;

import fr.cleia.sia.domain.description.models.Fonds;

import java.util.Optional;

public interface DepotDeFonds {
    Fonds sauvegarderFonds(Fonds fonds);
    Optional<Fonds> findById(String identifiant);
}
