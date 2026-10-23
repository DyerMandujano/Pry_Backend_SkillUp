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
import com.skillup.api.rest.model.RespuestaEstudiante;
import com.skillup.api.rest.service.RespuestaEstudianteService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RespuestaEstudianteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RespuestaEstudianteService respuestaService;

    @InjectMocks
    private RespuestaEstudianteController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-RESP-01: GET /api/respuestas retorna lista de respuestas")
    void testListarRespuestas() throws Exception {
        RespuestaEstudiante r = new RespuestaEstudiante();
        r.setIdRespEstudiante(1);
        r.setPuntaje(5);

        when(respuestaService.listarRespuestas()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/respuestas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRespEstudiante").value(1));

        verify(respuestaService, times(1)).listarRespuestas();
    }

    @Test
    @DisplayName("REST-RESP-02: GET /api/respuestas/{id} retorna respuesta por ID")
    void testBuscarPorId() throws Exception {
        RespuestaEstudiante r = new RespuestaEstudiante();
        r.setIdRespEstudiante(10);
        r.setPuntaje(5);

        when(respuestaService.buscarPorId(10)).thenReturn(r);

        mockMvc.perform(get("/api/respuestas/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRespEstudiante").value(10));

        verify(respuestaService, times(1)).buscarPorId(10);
    }

    @Test
    @DisplayName("REST-RESP-03: POST /api/respuestas crea respuesta de estudiante")
    void testCrearRespuesta() throws Exception {
        RespuestaEstudiante r = new RespuestaEstudiante();
        r.setPuntaje(4);
        r.setTextoRespuesta("Respuesta dada");

        RespuestaEstudiante guardada = new RespuestaEstudiante();
        guardada.setIdRespEstudiante(20);
        guardada.setPuntaje(4);

        when(respuestaService.guardarRespuesta(any(RespuestaEstudiante.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/respuestas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRespEstudiante").value(20));

        verify(respuestaService, times(1)).guardarRespuesta(any(RespuestaEstudiante.class));
    }

    @Test
    @DisplayName("REST-RESP-04: DELETE /api/respuestas/{id} elimina respuesta")
    void testEliminarRespuesta() throws Exception {
        doNothing().when(respuestaService).eliminarRespuesta(5);

        mockMvc.perform(delete("/api/respuestas/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().string("Respuesta eliminada correctamente"));

        verify(respuestaService, times(1)).eliminarRespuesta(5);
    }
}
