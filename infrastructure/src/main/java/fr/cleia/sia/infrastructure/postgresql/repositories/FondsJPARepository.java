package fr.cleia.sia.infrastructure.postgresql.repositories;

import fr.cleia.sia.infrastructure.postgresql.entities.FondsEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FondsJPARepository extends JpaRepository<FondsEntity, String> {
    @EntityGraph
    Optional<FondsEntity> findById(String identifiant);
}
