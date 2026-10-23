package com.skillup.api.rest.controller;

import java.util.List;

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
import com.skillup.api.rest.model.PreguntaEvaluacion;
import com.skillup.api.rest.service.PreguntaEvaluacionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PreguntaEvaluacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PreguntaEvaluacionService preguntaEvaluacionService;

    @InjectMocks
    private PreguntaEvaluacionController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-PREG-01: GET /api/preguntas retorna lista de preguntas")
    void testListarPreguntas() throws Exception {
        PreguntaEvaluacion p = new PreguntaEvaluacion();
        p.setIdPregunta(1);
        p.setEnunciado("¿Qué es polimorfismo?");

        when(preguntaEvaluacionService.listarPreguntas()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/preguntas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPregunta").value(1));

        verify(preguntaEvaluacionService, times(1)).listarPreguntas();
    }

    @Test
    @DisplayName("REST-PREG-02: GET /api/preguntas/{id} retorna pregunta por ID")
    void testBuscarPorId() throws Exception {
        PreguntaEvaluacion p = new PreguntaEvaluacion();
        p.setIdPregunta(5);
        p.setEnunciado("¿Qué es una interfaz?");

        when(preguntaEvaluacionService.buscarPorId(5)).thenReturn(p);

        mockMvc.perform(get("/api/preguntas/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPregunta").value(5));

        verify(preguntaEvaluacionService, times(1)).buscarPorId(5);
    }

    @Test
    @DisplayName("REST-PREG-03: POST /api/preguntas crea nueva pregunta")
    void testCrearPregunta() throws Exception {
        PreguntaEvaluacion p = new PreguntaEvaluacion();
        p.setEnunciado("¿Cuál es la anotación de un Controller REST?");
        p.setTipoPregunta("Multiple");
        p.setPuntuacion(5);

        PreguntaEvaluacion guardada = new PreguntaEvaluacion();
        guardada.setIdPregunta(10);
        guardada.setEnunciado("¿Cuál es la anotación de un Controller REST?");

        when(preguntaEvaluacionService.guardarPreguntaEvaluacion(any(PreguntaEvaluacion.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/preguntas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPregunta").value(10));

        verify(preguntaEvaluacionService, times(1)).guardarPreguntaEvaluacion(any(PreguntaEvaluacion.class));
    }

    @Test
    @DisplayName("REST-PREG-04: DELETE /api/preguntas/{id} elimina pregunta")
    void testEliminarPregunta() throws Exception {
        doNothing().when(preguntaEvaluacionService).eliminarPregEvaluacion(3);

        mockMvc.perform(delete("/api/preguntas/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(content().string("Pregunta eliminada correctamente"));

        verify(preguntaEvaluacionService, times(1)).eliminarPregEvaluacion(3);
    }
}
