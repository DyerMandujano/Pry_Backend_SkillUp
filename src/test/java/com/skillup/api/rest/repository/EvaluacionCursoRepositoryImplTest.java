package com.skillup.api.rest.repository;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.EvaluacionCursoDTO;
import com.skillup.api.rest.repository.imp.EvaluacionCursoRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvaluacionCursoRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery query;

    @InjectMocks
    private EvaluacionCursoRepositoryImpl repository;

    @Test
    @DisplayName("REPO-EVAL-01: Mapeo correcto de SP_ListarEvaluacionCurso")
    void testListarEvaluacionPorSeccion() {
        when(entityManager.createStoredProcedureQuery("SP_ListarEvaluacionCurso")).thenReturn(query);
        when(query.registerStoredProcedureParameter("Id_Seccion", Integer.class, ParameterMode.IN)).thenReturn(query);
        when(query.setParameter("Id_Seccion", 5)).thenReturn(query);

        Object[] row = new Object[]{1, "¿Qué es Spring?", "Framework Java", "Lenguaje", "SO", 101, 102, 103};
        when(query.getResultList()).thenReturn(java.util.Collections.singletonList(row));

        List<EvaluacionCursoDTO> resultado = repository.listarEvaluacionPorSeccion(5);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdPregunta());
        assertEquals("¿Qué es Spring?", resultado.get(0).getEnunciado());
        assertEquals("Framework Java", resultado.get(0).getOpcion1());
        assertEquals(101, resultado.get(0).getIdOp1());
    }
}
