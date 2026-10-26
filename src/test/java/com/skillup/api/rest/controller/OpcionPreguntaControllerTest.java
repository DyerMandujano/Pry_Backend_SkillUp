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
import com.skillup.api.rest.model.OpcionPregunta;
import com.skillup.api.rest.service.OpcionPreguntaService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OpcionPreguntaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OpcionPreguntaService opcionPreguntaService;

    @InjectMocks
    private OpcionPreguntaController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-OPC-01: GET /api/opcionpreguntas retorna lista de opciones")
    void testListarOpciones() throws Exception {
        OpcionPregunta opc = new OpcionPregunta();
        opc.setIdOpcPregunta(1);
        opc.setOpcion("Opción A");

        when(opcionPreguntaService.listarOpcionPreguntas()).thenReturn(List.of(opc));

        mockMvc.perform(get("/api/opcionpreguntas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOpcPregunta").value(1));

        verify(opcionPreguntaService, times(1)).listarOpcionPreguntas();
    }

    @Test
    @DisplayName("REST-OPC-02: GET /api/opcionpreguntas/{id} retorna opción si existe")
    void testBuscarPorId() throws Exception {
        OpcionPregunta opc = new OpcionPregunta();
        opc.setIdOpcPregunta(3);
        opc.setOpcion("Opción B");

        when(opcionPreguntaService.buscarPorId(3)).thenReturn(opc);

        mockMvc.perform(get("/api/opcionpreguntas/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOpcPregunta").value(3));

        verify(opcionPreguntaService, times(1)).buscarPorId(3);
    }

    @Test
    @DisplayName("REST-OPC-03: POST /api/opcionpreguntas crea opción exitosamente")
    void testCrearOpcion() throws Exception {
        OpcionPregunta opc = new OpcionPregunta();
        opc.setOpcion("Opción Correcta");
        opc.setEsCorrecta("1");

        OpcionPregunta guardada = new OpcionPregunta();
        guardada.setIdOpcPregunta(10);
        guardada.setOpcion("Opción Correcta");

        when(opcionPreguntaService.guardarOpcionPregunta(any(OpcionPregunta.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/opcionpreguntas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(opc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOpcPregunta").value(10));

        verify(opcionPreguntaService, times(1)).guardarOpcionPregunta(any(OpcionPregunta.class));
    }

    @Test
    @DisplayName("REST-OPC-04: DELETE /api/opcionpreguntas/{id} elimina opción correctamente")
    void testEliminarOpcion() throws Exception {
        doNothing().when(opcionPreguntaService).eliminarOpcPregunta(5);

        mockMvc.perform(delete("/api/opcionpreguntas/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().string("Opción eliminada correctamente"));

        verify(opcionPreguntaService, times(1)).eliminarOpcPregunta(5);
    }
}
