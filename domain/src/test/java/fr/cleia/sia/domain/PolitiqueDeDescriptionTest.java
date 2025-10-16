package fr.cleia.sia.domain;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.rules.PolitiqueDeDescription;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PolitiqueDeDescriptionTest {
    /*
    @Test

    void echoue_si_arborescence_incomplete() {
        Fonds fonds = new Fonds("F1", "Fonds A");
        fonds.ajouterDossier(new Dossier("D1", "Dossier A", "A-001"));

        PolitiqueDeDescription metier = new PolitiqueDeDescription();

        assertThatThrownBy(() -> metier.verifierArborescenceComplete(fonds))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Arborescence Incomplète");
    }

    @Test
    void echoue_si_cote_dupliquee() {
        Fonds fonds = new Fonds("F1", "Fonds A");
        fonds.ajouterDossier(new Dossier("D1", "Dossier A", "A-001"));
        fonds.ajouterDossier(new Dossier("D2", "Dossier B", "A-001"));

        PolitiqueDeDescription metier = new PolitiqueDeDescription();

        assertThatThrownBy(() -> metier.verifierUniciteCotes(fonds))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cote déjà utilisée");
    }
    */
}