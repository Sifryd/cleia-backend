package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.*;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeplacerSousArbreDeltaConserve {
    @Test
    void deplacer_sous_arbre_delta_0_parent_maj(){
        // given

        var finder = mock(FinderDeNoeudArchivisitique.class);
        var archiveNodeRepository = mock(ArchiveNodeRepository.class);

        var idF = NodeId.newId();
        var idD = NodeId.newId();
        var idD2 = NodeId.newId();
        var idP = NodeId.newId();

        var F = new Fonds(idF, new Title("F"));
        var D2 = new Dossier(idD2, idF, new Depth(1), new Title("D2"));
        var D = new Dossier(idD, idF, new Depth(1), new Title("D"));
        var P = new Piece(idP, idD, new Depth(2), new Title("P"));

        when(archiveNodeRepository.findById(idD)).thenReturn(Optional.of(D));
        when(archiveNodeRepository.findById(idD2)).thenReturn(Optional.of(D2));

        when(finder.sousArbre(idD, -1)).thenReturn(Stream.of(D, P));

        var service = new ArbreArchivistiqueService(finder, archiveNodeRepository);

        // when
        service.deplacerSousArbre(idD, idD2);

        // then
        verify(archiveNodeRepository, times(1)).saveAll(argThat(nodes -> {
            var movedD = nodes.stream().filter(n -> n.identifiant().equals(idD)).findFirst().orElse(null);
            var movedP = nodes.stream().filter(n -> n.identifiant().equals(idP)).findFirst().orElse(null);

            if (!(movedD instanceof Dossier md) || !(movedP instanceof Piece mp)) {
                return false;
            }

            boolean dOk = md.parentId().equals(idD2) && md.getProfondeur().value() == 2;
            boolean pOk = mp.parentId().equals(idD2) && mp.getProfondeur().value() == 3;
            return dOk && pOk;
        }
        ));
    }

    @Test
    void deplacer_un_fonds_est_interdit(){
        // given
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var archiveNodeRepository = mock(ArchiveNodeRepository.class);


        var idF = NodeId.newId();
        var F = new Fonds(idF, new Title("F"));
        when(archiveNodeRepository.findById(idF)).thenReturn(Optional.of(F));

        var service = new ArbreArchivistiqueService(finder, archiveNodeRepository);

        //then
        assertThatThrownBy(() -> service.deplacerSousArbre(idF, NodeId.newId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("déplacer un Fonds");
    }
}
