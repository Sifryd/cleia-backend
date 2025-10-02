package fr.cleia.sia.application.usecase;

import fr.cleia.sia.application.ports.DepotDeFonds;
import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsulterArborescenceServiceTest {
    @Test
    void retourne_le_fonds_avec_les_dossiers_et_pieces(){
        // given
        DepotDeFonds depot = mock(DepotDeFonds.class);
        var fonds = new Fonds("F1", "Fonds A");
        var d = new Dossier("D1", "Dossier A", "A-001");
        d.ajouterPiece(new Piece("P1", "Piece 1"));
        fonds.ajouterDossier(d);
        when(depot.findById("F1")).thenReturn(Optional.of(fonds));

        var service = new ConsulterArborescenceService(depot);
        // when
        var res = service.executer("F1");

        // then
        assertThat(res.identifiantFonds()).isEqualTo("F1");
        assertThat(res.intitule()).isEqualTo("Fonds A");
        assertThat(res.dossiers()).hasSize(1);
        assertThat(res.dossiers().getFirst().pieces()).hasSize(1);
    }

    @Test
    void leve_une_400_si_inconnu(){
        DepotDeFonds depot = mock(DepotDeFonds.class);
        when(depot.findById("X")).thenReturn(Optional.empty());
        var service = new ConsulterArborescenceService(depot);

        assertThatThrownBy(()->service.executer("X"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Fonds introuvable");
    }
}
