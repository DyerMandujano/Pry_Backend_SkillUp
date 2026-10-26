package com.skillup.api.rest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.skillup.api.rest.repository.MatriculaRepository;
import com.skillup.api.rest.service.MatriculaService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MatriculaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MatriculaService matriculaService;

    @Mock
    private MatriculaRepository matriculaRepository;

    @InjectMocks
    private MatriculaController matriculaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(matriculaController).build();
    }

    @Test
    @DisplayName("REST-MAT-01: POST /api/matriculas/insertar registra matrícula exitosamente")
    void testInsertarMatricula() throws Exception {
        doNothing().when(matriculaService).insertarMatricula(1, 100);

        mockMvc.perform(post("/api/matriculas/insertar")
                .param("idEstudiante", "1")
                .param("idCurso", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Matrícula registrada correctamente"));

        verify(matriculaService, times(1)).insertarMatricula(1, 100);
    }

    @Test
    @DisplayName("REST-MAT-02: GET /api/matriculas/verificar verifica si estudiante está matriculado")
    void testVerificarMatricula() throws Exception {
        when(matriculaRepository.existsByIdEstudianteAndIdCurso(1, 100)).thenReturn(true);

        mockMvc.perform(get("/api/matriculas/verificar")
                .param("idEstudiante", "1")
                .param("idCurso", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(matriculaRepository, times(1)).existsByIdEstudianteAndIdCurso(1, 100);
    }
}
