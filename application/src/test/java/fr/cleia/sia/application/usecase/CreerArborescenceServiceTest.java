package fr.cleia.sia.application.usecase;

import fr.cleia.sia.application.ports.DepotDeFonds;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.rules.PolitiqueDeDescription;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class CreerArborescenceServiceTest {
    @Test
    void cree_un_fonds_valide_et_appelle_le_depot() {
        var depot = mock(DepotDeFonds.class);
        var politique = new PolitiqueDeDescription();
        when(depot.sauvegarderFonds(any(Fonds.class)))
                .thenAnswer(i -> i.getArgument(0));

        var service = new CreerArborescenceService(depot, politique);

        var cmd = new CreerArborescenceService
                .Commande("F1", "Fonds A",
                List.of(new CreerArborescenceService.Commande.DossierCommande(
                        "D1", "Dossier A", "A-001",
                        List.of(new CreerArborescenceService.Commande.PieceCommande(
                                "P1", "Pièce 1"
                        ))
                ))
        );

        var res = service.executer(cmd);

        assertThat(res.identifiantFond()).isEqualTo("F1");
        var cap = ArgumentCaptor.forClass(Fonds.class);
        verify(depot, times(1)).sauvegarderFonds(cap.capture());
        assertThat(cap.getValue().dossiers()).hasSize(1);
        assertThat(cap.getValue().dossiers().get(0).pieces()).hasSize(1);
    }

    @Test
    void echoue_si_arbo_incomplete_ou_invalide() {
        var depot = mock(DepotDeFonds.class);
        var politique = new PolitiqueDeDescription();
        var service = new CreerArborescenceService(depot, politique);

        var cmd = new CreerArborescence.Commande(
                "F1", "Fonds A",
                List.of(new CreerArborescence.Commande.DossierCommande(
                        "D1", "Dossier A", "A-001", List.of()
                ))
        );

        assertThrows(IllegalStateException.class, () -> service.executer(cmd));
        verify(depot, never()).sauvegarderFonds(any());

    }

}
