package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.repository.EvaluacionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private EvaluacionService evaluacionService;

    @Test
    @DisplayName("CP-EVAL-TITULO-01: Mostrar título evaluación por sección ejecuta SP_MostrarTituloEval")
    @SuppressWarnings("unchecked")
    void testMostrarTituloEvaluacionPorSeccion_Exitoso() {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setIdEvaluacion(1);
        evaluacion.setTitulo("Examen Módulo 1");

        when(entityManager.createStoredProcedureQuery("SP_MostrarTituloEval", Evaluacion.class))
                .thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.singletonList(evaluacion));

        List<Evaluacion> resultado = evaluacionService.MostrarTituloEvaluacionPorSeccion(10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Examen Módulo 1", resultado.get(0).getTitulo());

        verify(entityManager).createStoredProcedureQuery("SP_MostrarTituloEval", Evaluacion.class);
        verify(storedProcedureQuery).registerStoredProcedureParameter("id_seccion", Integer.class, ParameterMode.IN);
        verify(storedProcedureQuery).setParameter("id_seccion", 10);
    }
}
