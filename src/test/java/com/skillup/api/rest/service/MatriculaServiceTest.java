package com.skillup.api.rest.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class MatriculaServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private MatriculaService matriculaService;

    @Test
    @DisplayName("CP-MATRICULA-01: Insertar matrícula delega al SP_InsertMatricula con parámetros")
    void testInsertarMatricula_Exitoso() {
        when(entityManager.createStoredProcedureQuery("SP_InsertMatricula")).thenReturn(storedProcedureQuery);

        matriculaService.insertarMatricula(10, 5);

        verify(entityManager).createStoredProcedureQuery("SP_InsertMatricula");
        verify(storedProcedureQuery).setParameter("id_estudiante", 10);
        verify(storedProcedureQuery).setParameter("id_curso", 5);
        verify(storedProcedureQuery).execute();
    }
}
