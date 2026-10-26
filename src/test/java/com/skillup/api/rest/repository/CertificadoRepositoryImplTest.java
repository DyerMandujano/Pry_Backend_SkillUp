package com.skillup.api.rest.repository;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.CertificadoPorEstudiante;
import com.skillup.api.rest.repository.imp.CertificadoRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CertificadoRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery query;

    @InjectMocks
    private CertificadoRepositoryImpl repository;

    @Test
    @DisplayName("REPO-CERT-01: Mapeo correcto de SP_ListarCertificadoPorEstud")
    void testListarCertificadosPorEstudiante() {
        when(entityManager.createStoredProcedureQuery("SP_ListarCertificadoPorEstud")).thenReturn(query);
        when(query.registerStoredProcedureParameter("id_estudiante", Integer.class, ParameterMode.IN)).thenReturn(query);
        when(query.setParameter("id_estudiante", 10)).thenReturn(query);

        Object[] row = new Object[]{1, "Java Completo", 120, 10, "CERT-9988", "2026-10-22"};
        when(query.getResultList()).thenReturn(java.util.Collections.singletonList(row));

        List<CertificadoPorEstudiante> resultado = repository.listarCertificadosPorEstudiante(10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Java Completo", resultado.get(0).getNombreCurso());
        assertEquals("CERT-9988", resultado.get(0).getCodigoCertificado());
    }
}
