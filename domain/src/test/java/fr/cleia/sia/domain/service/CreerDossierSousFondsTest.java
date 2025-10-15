package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.ports.*;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreerDossierSousFondsTest {

    @Test
    void creer_dossier_sous_fonds_depth_1() {
        //given
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var archiveNodeRepository = mock(ArchiveNodeRepository.class);


        var idF = NodeId.newId();
        var idD = NodeId.newId();
        var fonds = new Fonds(idF, new Title("F"));

        when(archiveNodeRepository.findById(idF)).thenReturn(java.util.Optional.of(fonds));

        var service = new fr.cleia.sia.domain.service.ArbreArchivistiqueService(finder, archiveNodeRepository);

        //when
        var d = service.creerDossierSous(idF, idD, new Title("D"));

        //then
        assertThat(d.parentId()).isEqualTo(idF);
        assertThat(d.getProfondeur().value()).isEqualTo(1);
        verify(archiveNodeRepository).save(any(Dossier.class));
    }
}
