package fr.cleia.sia.domain.ports;

import fr.cleia.sia.domain.description.models.EntiteArchivistique;
import fr.cleia.sia.domain.vo.NodeId;

import java.util.Collection;
import java.util.Optional;

public interface ArchiveNodeRepository {
    EntiteArchivistique save(EntiteArchivistique entiteArchivistique);

    void saveAll(Collection<? extends EntiteArchivistique> nodes);

    Optional<EntiteArchivistique> findById(NodeId id);

    void deleteById(NodeId id);
}
