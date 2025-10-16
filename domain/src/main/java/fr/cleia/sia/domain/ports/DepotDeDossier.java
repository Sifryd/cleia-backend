package fr.cleia.sia.domain.ports;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.vo.NodeId;

import java.util.Optional;
import java.util.stream.Stream;

public interface DepotDeDossier {
    Optional<Dossier> findById(NodeId identifiant);
    void sauvegarderDossier(Dossier dossier);
    void supprimerDossier(NodeId identifiant);
    Stream<Dossier> enfantsDe(NodeId parentId);
    boolean existe(NodeId identifiant);
}
