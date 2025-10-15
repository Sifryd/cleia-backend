package fr.cleia.sia.domain.ports;

import fr.cleia.sia.domain.description.models.EntiteArchivistique;
import fr.cleia.sia.domain.vo.NodeId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface FinderDeNoeudArchivisitique {
    Stream<EntiteArchivistique> rechercherParTitre(String query, int limit);
    Optional<List<EntiteArchivistique>> chemin (NodeId id);
    Stream<EntiteArchivistique> sousArbre(NodeId parentId, int profondeurMax);
}
