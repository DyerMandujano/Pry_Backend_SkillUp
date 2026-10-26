package com.skillup.api.rest.controller;

import java.util.Date;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillup.api.rest.dtos.RegistrarReseniaRequestDTO;
import com.skillup.api.rest.dtos.ReseniaCursoDTO;
import com.skillup.api.rest.service.ReseniaCursoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ReseniaCursoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReseniaCursoService reseniaCursoService;

    @InjectMocks
    private ReseniaCursoController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-RES-01: GET /api/cursos/{idCurso}/resenias retorna lista de reseñas")
    void testListarResenias() throws Exception {
        ReseniaCursoDTO dto = new ReseniaCursoDTO(1, 10, 5, "Ana Lopez", 5, "Excelente curso", new Date(), 1);
        when(reseniaCursoService.listarReseniasPorCurso(10)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/cursos/{idCurso}/resenias", 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReseniaCurso").value(1))
                .andExpect(jsonPath("$[0].calificacion").value(5))
                .andExpect(jsonPath("$[0].comentario").value("Excelente curso"));

        verify(reseniaCursoService, times(1)).listarReseniasPorCurso(10);
    }

    @Test
    @DisplayName("REST-RES-02: POST /api/cursos/{idCurso}/resenias publica reseña exitosa")
    void testRegistrarResenia() throws Exception {
        RegistrarReseniaRequestDTO req = new RegistrarReseniaRequestDTO(5, 5, "Muy recomendado");
        ReseniaCursoDTO resp = new ReseniaCursoDTO(1, 10, 5, "Ana Lopez", 5, "Muy recomendado", new Date(), 1);

        when(reseniaCursoService.registrarResenia(eq(10), any(RegistrarReseniaRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/cursos/{idCurso}/resenias", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReseniaCurso").value(1))
                .andExpect(jsonPath("$.comentario").value("Muy recomendado"));

        verify(reseniaCursoService, times(1)).registrarResenia(eq(10), any(RegistrarReseniaRequestDTO.class));
    }

    @Test
    @DisplayName("REST-RES-03: Manejo de RuntimeException devuelve badRequest")
    void testErrorResenia() throws Exception {
        when(reseniaCursoService.listarReseniasPorCurso(99)).thenThrow(new RuntimeException("Curso no encontrado"));

        mockMvc.perform(get("/api/cursos/{idCurso}/resenias", 99))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Curso no encontrado"));
    }
}
