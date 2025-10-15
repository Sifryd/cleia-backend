package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.*;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefuserEnfantSousPieceTest {

    @Test
    void creer_dossier_sous_piece_est_interdit() {
        //given
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var archiveNodeRepository = mock(ArchiveNodeRepository.class);


        var idD = NodeId.newId();
        var idP = NodeId.newId();
        var idNewD = NodeId.newId();

        var parentD = new Dossier(idD, NodeId.newId(), new Depth(1), new Title("D"));
        var piece = new Piece(idP, idD, new Depth(2), new Title("P"));

        when(archiveNodeRepository.findById(idP)).thenReturn(Optional.of(piece));

        var service = new ArbreArchivistiqueService(finder, archiveNodeRepository);

        //when / then
        assertThatThrownBy(() -> service.creerDossierSous(idP, idNewD, new Title("X")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Parent refuse");
    }
}
