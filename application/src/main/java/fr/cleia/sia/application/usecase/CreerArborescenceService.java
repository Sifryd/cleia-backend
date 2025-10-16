package fr.cleia.sia.application.usecase;

import fr.cleia.sia.domain.description.models.Dossier;
import fr.cleia.sia.domain.description.models.EntiteArchivistique;
import fr.cleia.sia.domain.description.models.Fonds;
import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.description.rules.PolitiqueDeDescription;
import fr.cleia.sia.domain.ports.ArchiveNodeRepository;
import fr.cleia.sia.domain.vo.Depth;
import fr.cleia.sia.domain.vo.NodeId;
import fr.cleia.sia.domain.vo.Title;

import java.util.ArrayList;

public class CreerArborescenceService implements CreerArborescence {
    private final ArchiveNodeRepository archiveNodeRepository;
    private final PolitiqueDeDescription politique;

    public CreerArborescenceService(ArchiveNodeRepository archiveNodeRepository, PolitiqueDeDescription politique) {
        this.archiveNodeRepository = archiveNodeRepository;
        this.politique = politique;
    }

    @Override
    public Resultat executer(Commande commande) {
        var fondsId = NodeId.newId();
        var fonds = new Fonds(fondsId, new Title(commande.intituleFonds()));

        if (commande.dossiers() != null) {
            for (var dossierCommande : commande.dossiers()) {
                var dossierId = NodeId.newId();
                var dossier = new Dossier(
                        dossierId,
                        fondsId,
                        new Depth(1),
                        new Title(dossierCommande.intituleDossier())
                );

                if (dossierCommande.pieces() != null) {
                    for (var pieceCommande : dossierCommande.pieces()) {
                        var pieceId = NodeId.newId();
                        var piece = new Piece(
                                pieceId,
                                dossierId,
                                new Depth(2),
                                new Title(pieceCommande.intitulePiece())
                        );
                        dossier.ajouterPiece(piece);
                    }
                }
                fonds.ajouterDossier(dossier);
            }
        }

        politique.verifierArborescenceComplete(fonds);

        var nodesToSave = new ArrayList<EntiteArchivistique>();
        fonds.dossiers().forEach(d -> nodesToSave.addAll(d.pieces()));
        nodesToSave.addAll(fonds.dossiers());
        nodesToSave.add(fonds);
        archiveNodeRepository.saveAll(nodesToSave);

        return new Resultat(fonds.identifiant().toString());
    }
}
