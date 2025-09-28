package fr.cleia.sia.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cleia.sia.application.usecase.CreerArborescence;
import fr.cleia.sia.web.dto.CreerArborescenceCommandeDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArborescenceController.class)
class ArborescenceControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    CreerArborescence creerArborescence;


    @Test
    void post_creer_arborescence_retourne_201_avec_resultat() throws Exception {
        Mockito.when(creerArborescence.executer(any())).thenReturn(new CreerArborescence.Resultat("F1"));
        var dto = new CreerArborescenceCommandeDTO("F1", "Fonds A",
                List.of(new CreerArborescenceCommandeDTO.DossierDTO("D1", "Dossier A", "A-001",
                        List.of(new CreerArborescenceCommandeDTO.PieceDTO("P1", "Pièce 1"))
                ))
        );

        mvc.perform(post("/api/fonds")
                .contentType("application/json")
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identifiantFond").value("F1"));



    }
}
