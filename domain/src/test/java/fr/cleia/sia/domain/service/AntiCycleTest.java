package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.exception.DomainException;
import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.ports.FinderDeNoeudArchivisitique;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AntiCycleTest {

    @Test
    void deplace_sous_un_descendant_est_interdit() {
        // given

        var finder = mock(FinderDeNoeudArchivisitique.class);
        var archiveNodeRepository = mock(ArchiveNodeRepository.class);

        var idD = NodeId.newId();
        var idDChild = NodeId.newId();

        var D = new Dossier(idD, NodeId.newId(), new Depth(1), new Title("D"));
        var DChild = new Dossier(idDChild, idD, new Depth(2), new Title("DChild"));

        when(archiveNodeRepository.findById(idD)).thenReturn(Optional.of(D));
        when(archiveNodeRepository.findById(idDChild)).thenReturn(Optional.of(DChild));

        when(finder.sousArbre(idD, -1)).thenReturn(Stream.of(D, DChild));

        var service = new ArbreArchivistiqueService(finder, archiveNodeRepository);

        // then
        assertThatThrownBy(() -> service.deplacerSousArbre(idD, idDChild))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("descendants");
    }
}
