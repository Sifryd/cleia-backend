package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.exception.DomainError;
import fr.cleia.sia.domain.description.exception.DomainException;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.ports.FinderDeNoeudArchivisitique;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreationErreursTest {

    @Test
    void creer_dossier_sous_parent_inexistant_declenche_NODE_NOT_FOUND() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var parentId = NodeId.newId();
        when(repo.findById(parentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.creerDossierSous(parentId, NodeId.newId(), new Title("X")))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    org.assertj.core.api.Assertions.assertThat(de.error()).isEqualTo(DomainError.NODE_NOT_FOUND);
                });
    }

    @Test
    void creer_dossier_sous_piece_refuse_avec_contexte() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var dId = NodeId.newId();
        var pId = NodeId.newId();
        var piece = new Piece(pId, dId, new Depth(2), new Title("P"));

        when(repo.findById(pId)).thenReturn(Optional.of(piece));

        assertThatThrownBy(() -> service.creerDossierSous(pId, NodeId.newId(), new Title("X")))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    org.assertj.core.api.Assertions.assertThat(de.error()).isEqualTo(DomainError.PARENT_REFUSE_DOSSIER);
                    org.assertj.core.api.Assertions.assertThat(de.context())
                            .containsEntry("parentId", pId.toString());
                });
    }
}