package fr.cleia.sia.domain.description.rules;

import fr.cleia.sia.domain.description.models.Fonds;

import java.util.HashSet;
import java.util.Set;

public class PolitiqueDeDescription {
    public void verifierArborescenceComplete(Fonds fonds) {
        boolean auMoinsUnePiece = fonds.dossiers().stream()
                .anyMatch(d -> !d.pieces().isEmpty());
        if (!auMoinsUnePiece) {
            throw new IllegalStateException("Arborescence Incomplète : aucun dossier ne contient de pièce");
        }
    }

    public void verifierUniciteCotes(Fonds fonds) {
        Set<String> vues = new HashSet<>();
        fonds.dossiers().forEach(dossier ->
        {
            String cote = dossier.cote();
            if (!vues.add(cote)) {
                throw new IllegalStateException("Cote déjà utilisée " + cote);
            }
        });
    }
}
