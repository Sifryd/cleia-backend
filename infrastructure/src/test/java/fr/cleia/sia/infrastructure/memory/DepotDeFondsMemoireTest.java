package fr.cleia.sia.infrastructure.memory;

import fr.cleia.sia.domain.ports.DepotDeFonds;
import fr.cleia.sia.domain.description.models.Fonds;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DepotDeFondsMemoireTest {
    @Test
    void sauvegarder_retourne_le_fonds_periste(){
        DepotDeFonds depotDeFonds = new DepotDeFondsMemoire();
        var fonds = new Fonds("F1", "Fonds A");

        var resultat = depotDeFonds.sauvegarderFonds(fonds);

        assertThat(resultat).isSameAs(fonds);
    }
}
