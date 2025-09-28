package fr.cleia.sia.web.controller;

import fr.cleia.sia.application.usecase.CreerArborescence;
import fr.cleia.sia.web.dto.CreerArborescenceCommandeDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fonds")
public class ArborescenceController {
    private final CreerArborescence useCase;

    public ArborescenceController(CreerArborescence useCase){
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<?> creer (@Valid @RequestBody CreerArborescenceCommandeDTO dto){
        var commande = new CreerArborescence.Commande(
                dto.identifiantFond(),
                dto.intituleFonds(),
                dto.dossiers().stream().map(d ->
                        new CreerArborescence.Commande.DossierCommande(
                                d.identifiant(),
                                d.intitule(),
                                d.cote(),
                                d.pieces().stream().map(p ->
                                        new CreerArborescence.Commande.PieceCommande(
                                                p.identifiant(),
                                                p.intitule()
                                        )
                                ).toList()
                        )
                ).toList()
        );

        var res = useCase.executer(commande);
        return ResponseEntity.status(201).body(res);
    }
}
