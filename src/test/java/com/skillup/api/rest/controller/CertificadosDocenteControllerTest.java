package com.skillup.api.rest.controller;

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

import com.skillup.api.rest.dtos.CertificacionDocenteDTO;
import com.skillup.api.rest.service.CertificadosDocenteService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CertificadosDocenteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CertificadosDocenteService certificadosDocenteService;

    @InjectMocks
    private CertificadosDocenteController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-CERTDOC-01: GET /api/certificados/docente/{id} retorna certificaciones por docente")
    void testListarCertificadosDocente() throws Exception {
        CertificacionDocenteDTO dto = new CertificacionDocenteDTO();
        dto.setDocente("Profesor Carlos");
        dto.setIdCurso(10);
        dto.setNombreCurso("Spring Boot Avanzado");
        dto.setEstudiante("Juan Perez");
        dto.setCodigoCertificado("CERT-7788");

        when(certificadosDocenteService.listarPorDocente(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/certificados/docente/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].docente").value("Profesor Carlos"))
                .andExpect(jsonPath("$[0].nombreCurso").value("Spring Boot Avanzado"))
                .andExpect(jsonPath("$[0].codigoCertificado").value("CERT-7788"));

        verify(certificadosDocenteService, times(1)).listarPorDocente(1);
    }
}
