package fr.cleia.sia.infrastructure.postgresql.adapter;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.EntiteArchivistique;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.ports.DepotDeDossier;
import fr.cleia.sia.domain.ports.DepotDeFonds;
import fr.cleia.sia.domain.ports.DepotDePiece;
import fr.cleia.sia.domain.vo.NodeId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CompositeArchiveNodeRepository implements ArchiveNodeRepository {

    private final DepotDeFonds depotDeFonds;
    private final DepotDeDossier depotDeDossier;
    private final DepotDePiece depotDePiece;

    public CompositeArchiveNodeRepository(DepotDeFonds depotDeFonds, DepotDeDossier depotDeDossier, DepotDePiece depotDePiece) {
        this.depotDeFonds = depotDeFonds;
        this.depotDeDossier = depotDeDossier;
        this.depotDePiece = depotDePiece;
    }

    @Override
    public EntiteArchivistique save(EntiteArchivistique entiteArchivistique) {
        if (entiteArchivistique instanceof Fonds f) {
            depotDeFonds.sauvegarderFonds(f);
            return f;
        }
        if (entiteArchivistique instanceof Dossier d){
            depotDeDossier.sauvegarderDossier(d);
            return d;
        }
        if (entiteArchivistique instanceof Piece p ){
            depotDePiece.sauvegarderPiece(p);
            return p;
        }
        throw new IllegalArgumentException("Unknown entite archivistique type: " + entiteArchivistique.getClass());
    }

    @Override
    @Transactional
    public void saveAll(Collection<? extends EntiteArchivistique> nodes) {
        for (EntiteArchivistique node : nodes) {
            save(node);
        }
    }

    @Override
    public Optional<EntiteArchivistique> findById(NodeId id){
        return depotDeFonds.findById(id).map(f -> (EntiteArchivistique) f)
                .or(() -> depotDeDossier.findById(id).map(d -> (EntiteArchivistique) d))
                .or(() -> depotDePiece.findById(id).map(p -> (EntiteArchivistique) p));
    }

    @Override
    public void deleteById(NodeId id) {
        if (depotDeFonds.findById(id).isPresent()) {
            depotDeFonds.supprimerFonds(id);
        }
        else if (depotDeDossier.findById(id).isPresent()) {
            depotDeDossier.supprimerDossier(id);
        }
        else {
            depotDePiece.supprimerPiece(id);
        }
    }
}
