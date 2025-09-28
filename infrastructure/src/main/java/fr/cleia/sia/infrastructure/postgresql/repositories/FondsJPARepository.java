package fr.cleia.sia.infrastructure.postgresql.repositories;

import fr.cleia.sia.infrastructure.postgresql.entities.FondsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FondsJPARepository extends JpaRepository<FondsEntity, String> {}
