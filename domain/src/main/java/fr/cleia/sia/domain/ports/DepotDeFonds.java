package fr.cleia.sia.domain.ports;

import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.vo.NodeId;

import java.util.Optional;

public interface DepotDeFonds {
    Fonds sauvegarderFonds(Fonds fonds);
    Optional<Fonds> findById(NodeId identifiant);
    void supprimerFonds(NodeId id);
}
