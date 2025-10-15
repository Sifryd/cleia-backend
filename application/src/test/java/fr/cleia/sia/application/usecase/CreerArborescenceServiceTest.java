package fr.cleia.sia.application.usecase;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.ports.DepotDeFonds;
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
        var repo = mock(ArchiveNodeRepository.class);
        var politique = new PolitiqueDeDescription();
        var service = new CreerArborescenceService(repo, politique);
        var cmd = new CreerArborescence.Commande(
                "F1", "Fonds A",
                List.of(new CreerArborescence.Commande.DossierCommande(
                        "D1", "Dossier A", "A-001",
                        List.of(new CreerArborescence.Commande.PieceCommande("P1", "Piece A"))
                ))
        );

        var res = service.executer(cmd);

        assertThat(res.identifiantFond()).isNotBlank();

        // Capture des enregistrements
        var fondsCap = ArgumentCaptor.forClass(Fonds.class);
        var dossierCap = ArgumentCaptor.forClass(Dossier.class);

        assertThat(res.identifiantFond()).isNotBlank();

        verify(repo, times(1)).saveAll(argThat(col -> {
            boolean hasFonds = col.stream().anyMatch(Fonds.class::isInstance);
            boolean hasDossier = col.stream().anyMatch(Dossier.class::isInstance);
            boolean hasPiece = col.stream().anyMatch(Piece.class::isInstance);
            return hasFonds && hasDossier && hasPiece;
        }));
        verifyNoMoreInteractions(repo);


    }

    @Test
    void echoue_si_arbo_incomplete_ou_invalide() {
        var repo = mock(ArchiveNodeRepository.class);
        var politique = new PolitiqueDeDescription();
        var service = new CreerArborescenceService(repo, politique);

        var cmd = new CreerArborescence.Commande(
                "F1", "Fonds A",
                List.of(new CreerArborescence.Commande.DossierCommande(
                        "D1", "Dossier A", "A-001", List.of()
                ))
        );

        assertThrows(IllegalStateException.class, () -> service.executer(cmd));
        verify(repo, never()).saveAll(any());
    }
}
