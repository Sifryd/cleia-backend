package fr.cleia.sia.domain.service;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.ports.FinderDeNoeudArchivisitique;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RenommageEtCreationImbriqueeTest {
    @Test
    void renommer_conserve_parent_et_profondeur_et_persiste() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var fId = NodeId.newId();
        var dId = NodeId.newId();
        var d = new Dossier(dId, fId, new Depth(1), new Title("Old"));
        when(repo.findById(dId)).thenReturn(Optional.of(d));

        var renommé = service.renommer(dId, new Title("New"));

        assertThat(renommé.identifiant()).isEqualTo(dId);
        assertThat(((Dossier) renommé).parentId()).isEqualTo(fId);
        assertThat(renommé.getProfondeur().value()).isEqualTo(1);
        assertThat(renommé.getIntitule().value()).isEqualTo("New");

        verify(repo).save(renommé);
    }

    @Test
    void creation_imbriquee_dossier_sous_dossier_depth_parent_plus_un() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var fId = NodeId.newId();
        var dParentId = NodeId.newId();
        var dParent = new Dossier(dParentId, fId, new Depth(2), new Title("Parent"));
        when(repo.findById(dParentId)).thenReturn(Optional.of(dParent));

        var newId = NodeId.newId();
        var created = service.creerDossierSous(dParentId, newId, new Title("Child"));

        assertThat(created.parentId()).isEqualTo(dParentId);
        assertThat(created.getProfondeur().value()).isEqualTo(3); // 2 + 1
        verify(repo).save(created);
    }

    @Test
    void creation_imbriquee_piece_sous_dossier_depth_parent_plus_un() {
        var repo = mock(ArchiveNodeRepository.class);
        var finder = mock(FinderDeNoeudArchivisitique.class);
        var service = new ArbreArchivistiqueService(finder, repo);

        var fId = NodeId.newId();
        var dParentId = NodeId.newId();
        var dParent = new Dossier(dParentId, fId, new Depth(1), new Title("Parent"));
        when(repo.findById(dParentId)).thenReturn(Optional.of(dParent));

        var newId = NodeId.newId();
        var piece = service.creerPieceSous(dParentId, newId, new Title("Leaf"));

        assertThat(piece.parentId()).isEqualTo(dParentId);
        assertThat(piece.getProfondeur().value()).isEqualTo(2); // 1 + 1
        verify(repo).save(piece);
    }
}
