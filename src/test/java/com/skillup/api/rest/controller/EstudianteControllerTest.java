package com.skillup.api.rest.controller;

import java.util.Collections;
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

import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.service.EstudianteService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EstudianteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EstudianteService estudianteService;

    @InjectMocks
    private EstudianteController estudianteController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(estudianteController).build();
    }

    @Test
    @DisplayName("REST-EST-01: GET /api/estudiantes retorna lista de estudiantes")
    void testListarTodos() throws Exception {
        Estudiante est = new Estudiante();
        est.setIdEstudiante(1);
        when(estudianteService.listarEstudiantes()).thenReturn(List.of(est));

        mockMvc.perform(get("/api/estudiantes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEstudiante").value(1));

        verify(estudianteService, times(1)).listarEstudiantes();
    }

    @Test
    @DisplayName("REST-EST-02: GET /api/estudiantes/{id} retorna un estudiante existente")
    void testObtenerUno() throws Exception {
        Estudiante est = new Estudiante();
        est.setIdEstudiante(5);
        when(estudianteService.obtenerEstudiante(5)).thenReturn(est);

        mockMvc.perform(get("/api/estudiantes/{idEstudiante}", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstudiante").value(5));

        verify(estudianteService, times(1)).obtenerEstudiante(5);
    }

    @Test
    @DisplayName("REST-EST-03: GET /api/estudiantes/{id} retorna bad request ante excepción")
    void testObtenerUnoError() throws Exception {
        when(estudianteService.obtenerEstudiante(99)).thenThrow(new RuntimeException("Estudiante no encontrado"));

        mockMvc.perform(get("/api/estudiantes/{idEstudiante}", 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Estudiante no encontrado"));
    }
}
