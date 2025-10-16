package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.exception.DomainError;
import fr.cleia.sia.domain.description.exception.DomainException;
import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.ports.FinderDeNoeudArchivisitique;
import fr.cleia.sia.domain.vo.NodeId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArbreArchivistiqueServiceSuppressionNotFoundTest {
    @Test
    void supprimer_noeud_inexistant_declenche_NODE_NOT_FOUND() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var id = NodeId.newId();

        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deplacerSousArbre(id, NodeId.newId()))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    org.assertj.core.api.Assertions.assertThat(de.error()).isEqualTo(DomainError.NODE_NOT_FOUND);
                });
    }
}
