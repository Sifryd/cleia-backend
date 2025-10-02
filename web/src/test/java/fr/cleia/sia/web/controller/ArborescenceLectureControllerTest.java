package fr.cleia.sia.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cleia.sia.application.usecase.ConsulterArborescence;
import fr.cleia.sia.web.errors.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArborescenceController.class)
@Import(ApiExceptionHandler.class)
class ArborescenceLectureControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    fr.cleia.sia.application.usecase.ConsulterArborescence consulter;

    @MockitoBean
    fr.cleia.sia.application.usecase.CreerArborescence creer;

    @Test
        void get_retourne_200_avec_arborescence () throws Exception{
        var res = new ConsulterArborescence.Resultat(
                "F1", "Fonds A",
                List.of(new ConsulterArborescence.Resultat.DossierR(
                        "D1", "Dossier A", "A-001",
                        List.of( new ConsulterArborescence.Resultat.PieceR("P1", "Pièce 1"))
                ))
        );
        when(consulter.executer("F1")).thenReturn(res);
        mvc.perform(get("/api/fonds/F1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifiantFonds").value("F1"))
                .andExpect(jsonPath("$.dossiers[0].pieces[0].identifiant").value("P1"));
    }
}
