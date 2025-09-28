package fr.cleia.sia.infrastructure.postgres;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.infrastructure.postgresql.adapter.DepotDeFondsJPA;
import fr.cleia.sia.infrastructure.postgresql.mapper.FondsMapper;
import fr.cleia.sia.infrastructure.postgresql.repositories.FondsJPARepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@DataJpaTest
@Import({ fr.cleia.sia.infrastructure.TestBootConfig.class,
        fr.cleia.sia.infrastructure.postgresql.mapper.FondsMapperImpl.class })
class DepotDeFondsJpaIT {

    @Container
    static final PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("cleia")
            .withUsername("cleia")
            .withPassword("cleia");

    @DynamicPropertySource
    static void props (DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", pg::getJdbcUrl);
        registry.add("spring.datasource.username", pg::getUsername);
        registry.add("spring.datasource.password", pg::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @org.springframework.beans.factory.annotation.Autowired
    FondsJPARepository repository;

    @org.springframework.beans.factory.annotation.Autowired
    FondsMapper mapper;

    @Test
    void persister_un_fonds_avec_dossier_piece(){
        var depot = new DepotDeFondsJPA(repository, mapper);
        var fonds = new Fonds("F1", "Fonds A");
        var dossier = new Dossier("D1", "Dossier A", "A-001");
        dossier.ajouterPiece(new Piece("P1", "Pièce 1"));
        fonds.ajouterDossier(dossier);

        depot.sauvegarderFonds(fonds);

        var e = repository.findById("F1").orElseThrow();
        assertThat(e.getDossiers()).hasSize(1);
        assertThat(e.getDossiers().get(0).getPieces()).hasSize(1);

    }

}
