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

import com.skillup.api.rest.dtos.CertificacionDocenteDTO;
import com.skillup.api.rest.repository.CertificadosDocenteRepository;

@ExtendWith(MockitoExtension.class)
public class CertificadosDocenteServiceTest {

    @Mock
    private CertificadosDocenteRepository certificadosDocenteRepository;

    @InjectMocks
    private CertificadosDocenteService certificadosDocenteService;

    @Test
    @DisplayName("CP-CERT-DOC-01: Listar certificados por docente retorna certificados emitidos")
    void testListarPorDocente_Exitoso() {
        CertificacionDocenteDTO dto = new CertificacionDocenteDTO();
        dto.setIdCurso(10);
        dto.setEstudiante("Juan Pérez");
        dto.setNombreCurso("Desarrollo Web Fullstack");

        when(certificadosDocenteRepository.listarCertificadosPorDocente(5))
                .thenReturn(Collections.singletonList(dto));

        List<CertificacionDocenteDTO> resultado = certificadosDocenteService.listarPorDocente(5);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getEstudiante());
        assertEquals("Desarrollo Web Fullstack", resultado.get(0).getNombreCurso());
        verify(certificadosDocenteRepository).listarCertificadosPorDocente(5);
    }

    @Test
    @DisplayName("CP-CERT-DOC-02: Listar certificados de docente sin alumnos certificados retorna lista vacía")
    void testListarPorDocente_Vacio() {
        when(certificadosDocenteRepository.listarCertificadosPorDocente(99))
                .thenReturn(Collections.emptyList());

        List<CertificacionDocenteDTO> resultado = certificadosDocenteService.listarPorDocente(99);

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }
}
