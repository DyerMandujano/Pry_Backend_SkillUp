package com.skillup.api.rest.controller;

import java.util.List;
import java.util.Map;

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

import com.skillup.api.rest.service.DocenteDashboardService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DocenteDashboardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DocenteDashboardService docenteDashboardService;

    @InjectMocks
    private DocenteDashboardController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-DASH-01: GET /api/docente-dashboard/certificados-por-curso/{idDocente} retorna lista")
    void testGetCertificadosPorCurso() throws Exception {
        Map<String, Object> map = Map.of("curso", "Angular", "totalCertificados", 12);
        when(docenteDashboardService.getCertificadosPorCurso(1)).thenReturn(List.of(map));

        mockMvc.perform(get("/api/docente-dashboard/certificados-por-curso/{idDocente}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].curso").value("Angular"));

        verify(docenteDashboardService, times(1)).getCertificadosPorCurso(1);
    }

    @Test
    @DisplayName("REST-DASH-02: GET /api/docente-dashboard/ranking-cursos/{idDocente} retorna ranking")
    void testGetRankingCursos() throws Exception {
        Map<String, Object> map = Map.of("curso", "Spring Boot", "posicion", 1);
        when(docenteDashboardService.getRankingCursos(1)).thenReturn(List.of(map));

        mockMvc.perform(get("/api/docente-dashboard/ranking-cursos/{idDocente}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].curso").value("Spring Boot"));

        verify(docenteDashboardService, times(1)).getRankingCursos(1);
    }

    @Test
    @DisplayName("REST-DASH-03: GET /api/docente-dashboard/tendencia-certificados/{idDocente} retorna tendencia")
    void testGetTendenciaCertificados() throws Exception {
        Map<String, Object> map = Map.of("mes", "Octubre", "cantidad", 25);
        when(docenteDashboardService.getTendenciaCertificados(1)).thenReturn(List.of(map));

        mockMvc.perform(get("/api/docente-dashboard/tendencia-certificados/{idDocente}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mes").value("Octubre"));

        verify(docenteDashboardService, times(1)).getTendenciaCertificados(1);
    }

    @Test
    @DisplayName("REST-DASH-04: Manejo de excepción devuelve badRequest")
    void testManejoError() throws Exception {
        when(docenteDashboardService.getTendenciaMatriculas(99)).thenThrow(new RuntimeException("Docente no encontrado"));

        mockMvc.perform(get("/api/docente-dashboard/tendencia-matriculas/{idDocente}", 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Docente no encontrado"));
    }
}
