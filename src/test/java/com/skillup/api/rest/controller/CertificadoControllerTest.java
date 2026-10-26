package com.skillup.api.rest.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

import com.skillup.api.rest.dtos.CertificadoPorEstudiante;
import com.skillup.api.rest.model.Certificado;
import com.skillup.api.rest.repository.CertificadoRepository;
import com.skillup.api.rest.service.CertificadoService;
import com.skillup.api.rest.service.EvaluacionCursoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CertificadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CertificadoService certificadoService;

    @Mock
    private CertificadoRepository certificadoRepository;

    @Mock
    private EvaluacionCursoService evaluacionCursoService;

    @InjectMocks
    private CertificadoController certificadoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(certificadoController).build();
    }

    @Test
    @DisplayName("REST-CERT-01: GET /api/certificados/estudiante/{id} lista certificados")
    void testListarCertificadosPorEstudiante() throws Exception {
        CertificadoPorEstudiante dto = new CertificadoPorEstudiante();
        dto.setIdCurso(1);
        dto.setNombreCurso("Curso Java");
        dto.setCodigoCertificado("CERT-1234");
        when(certificadoService.listarCertificados(10)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/certificados/estudiante/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCurso").value("Curso Java"));

        verify(certificadoService, times(1)).listarCertificados(10);
    }

    @Test
    @DisplayName("REST-CERT-02: POST /api/certificados/generar falla si no ha aprobado evaluacion")
    void testGenerarNoAprobado() throws Exception {
        when(evaluacionCursoService.haAprobadoEvaluacion(10, 1)).thenReturn(false);

        mockMvc.perform(post("/api/certificados/generar")
                .param("idEstudiante", "10")
                .param("idCurso", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Debes contestar todas las preguntas")));
    }

    @Test
    @DisplayName("REST-CERT-03: POST /api/certificados/generar retorna certificado existente si ya fue generado")
    void testGenerarExistente() throws Exception {
        when(evaluacionCursoService.haAprobadoEvaluacion(10, 1)).thenReturn(true);
        Certificado existente = new Certificado();
        existente.setIdCertificado(5);
        existente.setCodigoCertificado("CERT-EXISTENTE");
        when(certificadoRepository.findByEstudianteAndCurso(10, 1)).thenReturn(Optional.of(existente));

        mockMvc.perform(post("/api/certificados/generar")
                .param("idEstudiante", "10")
                .param("idCurso", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoCertificado").value("CERT-EXISTENTE"));
    }

    @Test
    @DisplayName("REST-CERT-04: POST /api/certificados/generar genera y guarda nuevo certificado")
    void testGenerarNuevoExitoso() throws Exception {
        when(evaluacionCursoService.haAprobadoEvaluacion(10, 1)).thenReturn(true);
        when(certificadoRepository.findByEstudianteAndCurso(10, 1)).thenReturn(Optional.empty());
        when(certificadoRepository.findIdMatricula(10, 1)).thenReturn(99);

        Certificado guardado = new Certificado();
        guardado.setIdCertificado(1);
        guardado.setIdMatricula(99);
        guardado.setCodigoCertificado("CERT-NUEVO");
        when(certificadoRepository.save(any(Certificado.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/certificados/generar")
                .param("idEstudiante", "10")
                .param("idCurso", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMatricula").value(99));

        verify(certificadoRepository, times(1)).save(any(Certificado.class));
    }
}
