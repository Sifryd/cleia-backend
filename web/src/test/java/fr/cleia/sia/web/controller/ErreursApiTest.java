package fr.cleia.sia.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cleia.sia.application.usecase.CreerArborescence;
import fr.cleia.sia.web.dto.CreerArborescenceCommandeDTO;
import fr.cleia.sia.web.errors.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArborescenceController.class)
@Import(ApiExceptionHandler.class)
class ErreursApiTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    CreerArborescence useCase;

    @Test
    void renvoi_422_si_arborescence_incomplete() throws Exception{
        when(useCase.executer(any())).thenThrow(new IllegalStateException("Arborescence incomplète"));
        var body = new CreerArborescenceCommandeDTO("F1", "Fonds A",
                List.of(new CreerArborescenceCommandeDTO.DossierDTO("D1", "Dossier A", "A-001", List.of())));
        mvc.perform(post("/api/fonds").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value(containsString("Arborescence incomplète")));

    }
}
