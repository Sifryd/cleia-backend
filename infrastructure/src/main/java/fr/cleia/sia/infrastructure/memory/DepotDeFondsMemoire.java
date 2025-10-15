package fr.cleia.sia.infrastructure.memory;

import fr.cleia.sia.domain.ports.DepotDeFonds;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.vo.NodeId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DepotDeFondsMemoire implements DepotDeFonds {
    private final Map<String , Fonds> store = new ConcurrentHashMap<>();

    @Override
    public Fonds sauvegarderFonds(Fonds fonds) {
        store.put(fonds.identifiant(), fonds);
        return fonds;
    }

    @Override
    public Optional<Fonds> findById(NodeId identifiant) {
        return Optional.empty();
    }

    public Fonds recupererFonds(String identifiant) {
        return store.get(identifiant);
    }
}
