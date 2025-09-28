package fr.cleia.sia.infrastructure.postgres;

import fr.cleia.sia.application.ports.DepotDeFonds;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.infrastructure.memory.DepotDeFondsMemoire;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;


class DepotDeFondsPostgresTest {

    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void start(){
        pg.start();
    }
    @Test
    void sauvegarder_un_fonds_avec_dossier_piece(){
        DepotDeFonds depotDeFonds = new DepotDeFondsMemoire();
        var fonds = new Fonds("F1", "Fonds A");

        var resultat = depotDeFonds.sauvegarderFonds(fonds);

        assertThat(resultat).isSameAs(fonds);
    }
}
