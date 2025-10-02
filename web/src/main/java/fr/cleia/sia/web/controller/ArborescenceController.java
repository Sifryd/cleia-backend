package fr.cleia.sia.web.controller;

import fr.cleia.sia.application.usecase.ConsulterArborescence;
import fr.cleia.sia.application.usecase.CreerArborescence;
import fr.cleia.sia.web.dto.CreerArborescenceCommandeDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fonds")
public class ArborescenceController {
    private final CreerArborescence creerUseCase;
    private final ConsulterArborescence consulterUseCase;

    public ArborescenceController(CreerArborescence creerUseCase,
                                  ConsulterArborescence consulterUseCase) {
        this.creerUseCase = creerUseCase;
        this.consulterUseCase = consulterUseCase;
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

        var res = creerUseCase.executer(commande);
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> lire(@PathVariable("id") String id) {
        var r = consulterUseCase.executer(id);
        return ResponseEntity.ok(r);
    }
}
