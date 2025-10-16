package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.exception.DomainError;
import fr.cleia.sia.domain.description.exception.DomainException;
import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.*;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ArbreArchivistiqueServiceDeplacementTest {
    @Test
    void deplacer_sous_arbre_delta_conserve(){
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var idF = NodeId.newId();
        var idD = NodeId.newId();
        var idD2 = NodeId.newId();
        var idP = NodeId.newId();
        var newParentId = NodeId.newId();

        var fonds = new Fonds(idF, new Title("F"));
        var dossier = new Dossier(idD, idF, new Depth(1), new Title("D"));
        var dossier2 = new Dossier(idD2, idD, new Depth(2), new Title("D2"));
        var piece = new Piece(idP, idD2, new Depth(3), new Title("P"));

        var newParent = new Dossier(newParentId, idF, new Depth(1), new Title("NP"));

        when(repo.findById(idD)).thenReturn(Optional.of(dossier));
        when(repo.findById(newParentId)).thenReturn(Optional.of(newParent));

        when(finder.sousArbre(idD, -1)).thenReturn(Stream.of(dossier, dossier2, piece));

        service.deplacerSousArbre(idD, newParentId);

        verify(repo, times(1)).saveAll(argThat(nodes -> {
            var list = List.copyOf(nodes);
            var movedD = list.stream().filter(dd -> dd.identifiant().equals(idD)).findFirst().orElse(null);
            var movedD2 = list.stream().filter(dd2 -> dd2.identifiant().equals(idD2)).findFirst().orElse(null);
            var movedP = list.stream().filter(pp -> pp.identifiant().equals(idP)).findFirst().orElse(null);
            return movedD.parentId().equals(newParentId)
                    && movedD.getProfondeur().value() == 2
                    && movedD2.getProfondeur().value() == 3
                    && movedP.getProfondeur().value() == 4;
        }));
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

    @Test
    void deplacer_sous_soi_est_interdit() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var idF = NodeId.newId();
        var idD = NodeId.newId();
        var d = new Dossier(idD, idF, new Depth(1), new Title("D"));

        when(repo.findById(idD)).thenReturn(Optional.of(d));

        assertThatThrownBy(() -> service.deplacerSousArbre(idD, idD))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    org.assertj.core.api.Assertions.assertThat(de.error()).isEqualTo(DomainError.MOVE_SELF_FORBIDDEN);
                    org.assertj.core.api.Assertions.assertThat(de.context())
                            .containsEntry("nodeId", idD.toString())
                            .containsEntry("newParentId", idD.toString());
                });
    }

    @Test
    void deplacer_sous_un_parent_incompatible_est_interdit() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var idF = NodeId.newId();
        var idD = NodeId.newId();
        var idP = NodeId.newId();

        var dossier = new Dossier(idD, idF, new Depth(1), new Title("D"));
        var pieceParent = new Piece(idP, idD, new Depth(2), new Title("P"));

        when(repo.findById(idD)).thenReturn(Optional.of(dossier));
        when(repo.findById(idP)).thenReturn(Optional.of(pieceParent));

        assertThatThrownBy(() -> service.deplacerSousArbre(idD, idP))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    org.assertj.core.api.Assertions.assertThat(de.error()).isEqualTo(DomainError.PARENT_REFUSE_DOSSIER);
                    org.assertj.core.api.Assertions.assertThat(de.context())
                            .containsEntry("nodeId", idD.toString())
                            .containsEntry("newParentId", idP.toString())
                            .containsEntry("childType", "DOSSIER");
                });
    }

    @Test
    void deplacer_sous_un_descendant_est_interdit() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var dId = NodeId.newId();
        var childId = NodeId.newId();
        var fId = NodeId.newId();

        var d = new Dossier(dId, fId, new Depth(1), new Title("D"));
        var child = new Dossier(childId, dId, new Depth(2), new Title("C"));

        when(repo.findById(dId)).thenReturn(Optional.of(d));
        when(repo.findById(childId)).thenReturn(Optional.of(child));
        when(finder.sousArbre(dId, -1)).thenReturn(Stream.of(d, child));

        assertThatThrownBy(() -> service.deplacerSousArbre(dId, childId))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> assertThat(((DomainException) ex).error()).isEqualTo(DomainError.MOVE_DESC_FORBIDDEN));
    }

}
