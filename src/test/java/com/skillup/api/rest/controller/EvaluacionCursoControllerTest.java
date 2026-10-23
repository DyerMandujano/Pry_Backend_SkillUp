package com.skillup.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillup.api.rest.dtos.EvaluacionCursoDTO;
import com.skillup.api.rest.dtos.EvaluacionEstadoDTO;
import com.skillup.api.rest.dtos.EvaluacionResultadoDTO;
import com.skillup.api.rest.dtos.EvaluacionSubmitDTO;
import com.skillup.api.rest.service.EvaluacionCursoService;

@ExtendWith(MockitoExtension.class)
public class EvaluacionCursoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EvaluacionCursoService evaluacionCursoService;

    @InjectMocks
    private EvaluacionCursoController evaluacionCursoController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(evaluacionCursoController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("REST-EVAL-01: GET /api/evaluacion/seccion/{id} retorna preguntas de evaluación")
    void testListarEvaluacion() throws Exception {
        EvaluacionCursoDTO dto = new EvaluacionCursoDTO();
        dto.setIdPregunta(1);
        dto.setEnunciado("¿Qué es Java?");

        when(evaluacionCursoService.listarEvaluacion(10)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/evaluacion/seccion/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPregunta").value(1))
                .andExpect(jsonPath("$[0].enunciado").value("¿Qué es Java?"));
    }

    @Test
    @DisplayName("REST-EVAL-02: GET /api/evaluacion/seccion/{id}/estado retorna intentos restantes")
    void testObtenerEstadoEvaluacion() throws Exception {
        EvaluacionEstadoDTO estado = new EvaluacionEstadoDTO();
        estado.setIdSeccion(10);
        estado.setIntentosRestantes(2);
        estado.setYaAprobado(false);

        when(evaluacionCursoService.obtenerEstadoEvaluacion(10, 5)).thenReturn(estado);

        mockMvc.perform(get("/api/evaluacion/seccion/10/estado")
                .param("idEstudiante", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intentosRestantes").value(2))
                .andExpect(jsonPath("$.yaAprobado").value(false));
    }

    @Test
    @DisplayName("REST-EVAL-03: POST /api/evaluacion/seccion/{id}/enviar califica evaluación y retorna resultado")
    void testEnviarEvaluacion() throws Exception {
        EvaluacionSubmitDTO submitDTO = new EvaluacionSubmitDTO();
        submitDTO.setIdEstudiante(5);

        EvaluacionResultadoDTO resultado = new EvaluacionResultadoDTO();
        resultado.setAprobado(true);
        resultado.setPuntajeTotal(20);
        resultado.setCorrectas(4);

        when(evaluacionCursoService.evaluarYGuardarRespuestas(eq(10), any(EvaluacionSubmitDTO.class)))
                .thenReturn(resultado);

        mockMvc.perform(post("/api/evaluacion/seccion/10/enviar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aprobado").value(true))
                .andExpect(jsonPath("$.puntajeTotal").value(20));
    }
}
