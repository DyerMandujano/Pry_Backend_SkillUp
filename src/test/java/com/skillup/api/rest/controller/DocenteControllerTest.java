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

import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.service.DocenteService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DocenteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DocenteService docenteService;

    @InjectMocks
    private DocenteController docenteController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(docenteController).build();
    }

    @Test
    @DisplayName("REST-DOC-01: GET /api/docentes retorna lista de docentes")
    void testListarTodos() throws Exception {
        Docente doc = new Docente();
        doc.setIdDocente(1);
        when(docenteService.listarTodosDocentes()).thenReturn(List.of(doc));

        mockMvc.perform(get("/api/docentes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDocente").value(1));

        verify(docenteService, times(1)).listarTodosDocentes();
    }

    @Test
    @DisplayName("REST-DOC-02: GET /api/docentes/{id} retorna un docente existente")
    void testObtenerUno() throws Exception {
        Docente doc = new Docente();
        doc.setIdDocente(3);
        when(docenteService.obtenerDocente(3)).thenReturn(doc);

        mockMvc.perform(get("/api/docentes/{idDocente}", 3)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDocente").value(3));

        verify(docenteService, times(1)).obtenerDocente(3);
    }

    @Test
    @DisplayName("REST-DOC-03: GET /api/docentes/{id} retorna bad request ante excepción")
    void testObtenerUnoError() throws Exception {
        when(docenteService.obtenerDocente(99)).thenThrow(new RuntimeException("Docente no encontrado"));

        mockMvc.perform(get("/api/docentes/{idDocente}", 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Docente no encontrado"));
    }
}
