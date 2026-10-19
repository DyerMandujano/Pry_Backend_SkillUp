package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.CertificadoPorEstudiante;
import com.skillup.api.rest.repository.CertificadoPorEstudianteRepository;

@ExtendWith(MockitoExtension.class)
public class CertificadoServiceTest {

    @Mock
    private CertificadoPorEstudianteRepository certificadoRepository;

    @InjectMocks
    private CertificadoService certificadoService;

    @Test
    @DisplayName("CP-CERT-01: Listar certificados de estudiante retorna sus diplomas emitidos")
    void testListarCertificados_RetornaCertificadosEstudiante() {
        CertificadoPorEstudiante cert1 = new CertificadoPorEstudiante();
        cert1.setIdEstudiante(10);
        cert1.setNombreCurso("Java Spring Boot");

        CertificadoPorEstudiante cert2 = new CertificadoPorEstudiante();
        cert2.setIdEstudiante(10);
        cert2.setNombreCurso("Angular Frontend");

        when(certificadoRepository.listarCertificadosPorEstudiante(10))
            .thenReturn(Arrays.asList(cert1, cert2));

        List<CertificadoPorEstudiante> resultado = certificadoService.listarCertificados(10);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Java Spring Boot", resultado.get(0).getNombreCurso());
        verify(certificadoRepository).listarCertificadosPorEstudiante(10);
    }

    @Test
    @DisplayName("CP-CERT-02: Estudiante sin certificados aprobados retorna lista vacía")
    void testListarCertificados_SinCertificados_RetornaVacio() {
        when(certificadoRepository.listarCertificadosPorEstudiante(99))
            .thenReturn(new ArrayList<>());

        List<CertificadoPorEstudiante> resultado = certificadoService.listarCertificados(99);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(certificadoRepository).listarCertificadosPorEstudiante(99);
    }
}
