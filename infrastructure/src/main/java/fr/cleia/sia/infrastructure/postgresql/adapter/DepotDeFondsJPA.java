package fr.cleia.sia.infrastructure.postgresql.adapter;

import fr.cleia.sia.domain.ports.DepotDeFonds;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.infrastructure.postgresql.entities.FondsEntity;
import fr.cleia.sia.infrastructure.postgresql.mapper.FondsMapper;
import fr.cleia.sia.infrastructure.postgresql.repositories.FondsJPARepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepotDeFondsJPA implements DepotDeFonds {
    private final FondsJPARepository fondsJPARepository;
    private final FondsMapper fondsMapper;

    public DepotDeFondsJPA(FondsJPARepository fondsJPARepository, FondsMapper fondsMapper) {
        this.fondsJPARepository = fondsJPARepository;
        this.fondsMapper = fondsMapper;
    }

    @Override
    public Fonds sauvegarderFonds(Fonds fonds){
        FondsEntity entity = fondsMapper.toEntity(fonds);
        fondsJPARepository.save(entity);
        return fonds;
    }

    @Override
    public Optional<Fonds> findById(NodeId identifiant) {
        return fondsJPARepository.findById(identifiant).map(fondsMapper::toDomain);
    }
}
