package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class DocenteDashboardServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private DocenteDashboardService docenteDashboardService;

    @Test
    @DisplayName("CP-DASHBOARD-01: getCertificadosPorCurso delega a SP_Dashboard_CertificadosPorCurso")
    @SuppressWarnings("unchecked")
    void testGetCertificadosPorCurso() {
        Map<String, Object> fila = new HashMap<>();
        fila.put("curso", "Java Intermedio");
        fila.put("total", 12);
        List<Map<String, Object>> mockList = Collections.singletonList(fila);

        when(entityManager.createStoredProcedureQuery("SP_Dashboard_CertificadosPorCurso")).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(mockList);

        List<Map<String, Object>> resultado = docenteDashboardService.getCertificadosPorCurso(5);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Java Intermedio", resultado.get(0).get("curso"));
        verify(storedProcedureQuery).registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        verify(storedProcedureQuery).setParameter("idDocente", 5);
    }

    @Test
    @DisplayName("CP-DASHBOARD-02: getRankingCursos delega a SP_Dashboard_RankingCursos")
    @SuppressWarnings("unchecked")
    void testGetRankingCursos() {
        Map<String, Object> fila = new HashMap<>();
        fila.put("nombre_curso", "Spring Boot Pro");
        fila.put("alumnos", 50);
        List<Map<String, Object>> mockList = Collections.singletonList(fila);

        when(entityManager.createStoredProcedureQuery("SP_Dashboard_RankingCursos")).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(mockList);

        List<Map<String, Object>> resultado = docenteDashboardService.getRankingCursos(5);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(50, resultado.get(0).get("alumnos"));
        verify(storedProcedureQuery).registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        verify(storedProcedureQuery).setParameter("idDocente", 5);
    }

    @Test
    @DisplayName("CP-DASHBOARD-03: getTendenciaCertificados delega a SP_Dashboard_TendenciaCertificados")
    @SuppressWarnings("unchecked")
    void testGetTendenciaCertificados() {
        when(entityManager.createStoredProcedureQuery("SP_Dashboard_TendenciaCertificados")).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.emptyList());

        List<Map<String, Object>> resultado = docenteDashboardService.getTendenciaCertificados(5);

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(storedProcedureQuery).registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        verify(storedProcedureQuery).setParameter("idDocente", 5);
    }

    @Test
    @DisplayName("CP-DASHBOARD-04: getTendenciaMatriculas delega a SP_Dashboard_TendenciaMatriculas")
    @SuppressWarnings("unchecked")
    void testGetTendenciaMatriculas() {
        when(entityManager.createStoredProcedureQuery("SP_Dashboard_TendenciaMatriculas")).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.emptyList());

        List<Map<String, Object>> resultado = docenteDashboardService.getTendenciaMatriculas(5);

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(storedProcedureQuery).registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        verify(storedProcedureQuery).setParameter("idDocente", 5);
    }

    @Test
    @DisplayName("CP-DASHBOARD-05: getEstudiantesPorCurso delega a SP_Dashboard_EstudiantesPorCurso")
    @SuppressWarnings("unchecked")
    void testGetEstudiantesPorCurso() {
        when(entityManager.createStoredProcedureQuery("SP_Dashboard_EstudiantesPorCurso")).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.emptyList());

        List<Map<String, Object>> resultado = docenteDashboardService.getEstudiantesPorCurso(5);

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(storedProcedureQuery).registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        verify(storedProcedureQuery).setParameter("idDocente", 5);
    }
}
