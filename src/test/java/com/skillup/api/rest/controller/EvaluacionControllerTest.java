package com.skillup.api.rest.controller;

import java.time.LocalDate;
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

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.service.EvaluacionService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EvaluacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EvaluacionService evaluacionService;

    @InjectMocks
    private EvaluacionController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-EVAL-01: GET /api/evaluaciones/seccion/{idSeccion} retorna evaluaciones")
    void testListarPorSeccion() throws Exception {
        Evaluacion eval = new Evaluacion();
        eval.setIdEvaluacion(1);
        eval.setIdSeccion(10);
        eval.setTitulo("Evaluación Parcial");
        eval.setFechaCreacion(LocalDate.now());

        when(evaluacionService.MostrarTituloEvaluacionPorSeccion(10)).thenReturn(List.of(eval));

        mockMvc.perform(get("/api/evaluaciones/seccion/{idSeccion}", 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEvaluacion").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Evaluación Parcial"));

        verify(evaluacionService, times(1)).MostrarTituloEvaluacionPorSeccion(10);
    }
}
