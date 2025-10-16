package fr.cleia.sia.application.usecase;

import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.ports.DepotDeFonds;
import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.FinderDeNoeudArchivisitique;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsulterArborescenceServiceTest {
    @Test
    void retourne_le_fonds_avec_les_dossiers_et_pieces(){
        // given
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);

        var fondsId = new NodeId(UUID.randomUUID());
        var dossierId = new NodeId(UUID.randomUUID());
        var pieceId = new NodeId(UUID.randomUUID());

        var fonds = new Fonds(fondsId, new Title("Fonds A"));
        var dossier = new Dossier(dossierId, fondsId, new Depth(1), new Title("Dossier A"));
        var piece = new Piece(pieceId, dossierId, new Depth(2), new Title("Piece 1"));

        when(repo.findById(fondsId)).thenReturn(Optional.of(fonds));
        when(finder.sousArbre(fondsId, 1)).thenReturn(Stream.of(fonds, dossier, piece));

        var service = new ConsulterArborescenceService(repo, finder);

        // when
        var res = service.executer(fondsId.toString());


        // then
        assertThat(res.identifiantFonds()).isEqualTo(fondsId.toString());
        assertThat(res.intitule()).isEqualTo("Fonds A");
        assertThat(res.dossiers()).hasSize(1);
        assertThat(res.dossiers().getFirst().pieces()).hasSize(1);
        assertThat(res.dossiers().getFirst().identifiant()).isEqualTo(dossierId.toString());
        assertThat(res.dossiers().getFirst().intitule()).isEqualTo("Dossier A");
        assertThat(res.dossiers().getFirst().pieces().getFirst().identifiant()).isEqualTo(pieceId.toString());
        assertThat(res.dossiers().getFirst().pieces().getFirst().intitule()).isEqualTo("Piece 1");
    }

    @Test
    void leve_une_400_si_inconnu(){
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);

        var unknownId = new NodeId(UUID.randomUUID());

        when(repo.findById(unknownId)).thenReturn(Optional.empty());

        var service = new ConsulterArborescenceService(repo, finder);

        assertThatThrownBy(()->service.executer(unknownId.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Fonds introuvable");
    }
}
