package fr.cleia.sia.infrastructure;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan("fr.cleia.sia.infrastructure.postgresql.entities")
@EnableJpaRepositories("fr.cleia.sia.infrastructure.postgresql.repositories")
public class TestBootConfig { }