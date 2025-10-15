package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.ports.*;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.Node;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SuppressionReglesTest {

    @Test
    void supprimer_noeuf_avec_enfants_est_interdit() {
        var depotF = mock(DepotDeFonds.class);
        var depotD = mock(DepotDeDossier.class);
        var depotP = mock(DepotDePiece.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var archiveNodeRepository = mock(ArchiveNodeRepository.class);


        var idF = NodeId.newId();
        var idD = NodeId.newId();

        var F = new Fonds(idF, new Title("F"));
        var D = new Dossier(idD, idF, new Depth(1), new Title("D"));

        when(depotD.findById(idD)).thenReturn(Optional.of(D));
        when(depotF.findById(any())).thenReturn(Optional.empty());
        when(depotP.findById(any())).thenReturn(Optional.empty());


        when(finder.sousArbre(idD, 1)).thenReturn(Stream.of(D, new Piece(NodeId.newId(), idD, new Depth(2), new Title("P"))));

        var service = new ArbreArchivistiqueService(finder, archiveNodeRepository);

        assertThatThrownBy(() -> service.supprimer(idD))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Impossible de supprimer un noeud qui a des enfants");


        verify(depotD, never()).supprimerDossier(any());
    }

    @Test
    void supprimer_noeuf_sans_enfants_est_autorise() {
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var archiveNodeRepository = mock(ArchiveNodeRepository.class);


        var idD = NodeId.newId();
        var D = new Dossier(idD, NodeId.newId(), new Depth(1), new Title("D"));
        when(archiveNodeRepository.findById(idD)).thenReturn(Optional.of(D));
        when(finder.sousArbre(idD, 1)).thenReturn(Stream.of(D));

        var service = new ArbreArchivistiqueService(finder, archiveNodeRepository);

        //when
        service.supprimer(idD);

        //then
        verify(archiveNodeRepository).deleteById(idD);
    }

}
