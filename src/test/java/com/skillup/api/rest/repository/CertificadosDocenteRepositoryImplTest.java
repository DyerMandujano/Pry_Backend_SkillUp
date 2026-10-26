package com.skillup.api.rest.repository;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.CertificacionDocenteDTO;
import com.skillup.api.rest.repository.imp.CertificadosDocenteRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CertificadosDocenteRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery query;

    @InjectMocks
    private CertificadosDocenteRepositoryImpl repository;

    @Test
    @DisplayName("REPO-DOC-01: Mapeo correcto de SP_CertificadosPorDoc")
    void testListarCertificadosPorDocente() {
        when(entityManager.createStoredProcedureQuery("SP_CertificadosPorDoc")).thenReturn(query);
        when(query.registerStoredProcedureParameter("id_docente", Integer.class, ParameterMode.IN)).thenReturn(query);
        when(query.setParameter("id_docente", 1)).thenReturn(query);

        Object[] row = new Object[]{"Prof. Juan", 5, "Spring Boot", "Avanzado", "Carlos Estudiante", "CERT-1122", "2026-10-22"};
        when(query.getResultList()).thenReturn(java.util.Collections.singletonList(row));

        List<CertificacionDocenteDTO> resultado = repository.listarCertificadosPorDocente(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Prof. Juan", resultado.get(0).getDocente());
        assertEquals("Spring Boot", resultado.get(0).getNombreCurso());
        assertEquals("Carlos Estudiante", resultado.get(0).getEstudiante());
    }
}
