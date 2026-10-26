package com.skillup.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

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
import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.service.CursoService;

@ExtendWith(MockitoExtension.class)
public class CursoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CursoService cursoService;

    @InjectMocks
    private CursoController cursoController;

    private ObjectMapper objectMapper;
    private Curso curso;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();
        objectMapper = new ObjectMapper();

        curso = new Curso();
        curso.setIdCurso(1);
        curso.setNombreCurso("Curso Spring Boot REST");
        curso.setEstado(1);
    }

    @Test
    @DisplayName("REST-CURSO-01: GET /api/cursos retorna 200 OK con lista de cursos")
    void testListarCursos() throws Exception {
        when(cursoService.listarCursos()).thenReturn(Collections.singletonList(curso));

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCurso").value(1))
                .andExpect(jsonPath("$[0].nombreCurso").value("Curso Spring Boot REST"));
    }

    @Test
    @DisplayName("REST-CURSO-02: GET /api/cursos/{id} retorna curso correspondiente")
    void testObtenerCursoPorId() throws Exception {
        when(cursoService.obtenerCursoPorId(1)).thenReturn(curso);

        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nombreCurso").value("Curso Spring Boot REST"));
    }

    @Test
    @DisplayName("REST-CURSO-03: POST /api/cursos registra nuevo curso")
    void testInsertarCurso() throws Exception {
        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Curso registrado correctamente")));

        verify(cursoService).insertarCurso(any(Curso.class));
    }

    @Test
    @DisplayName("REST-CURSO-04: PUT /api/cursos/{id} actualiza curso")
    void testActualizarCurso() throws Exception {
        mockMvc.perform(put("/api/cursos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Curso actualizado correctamente")));

        verify(cursoService).actualizarCurso(any(Curso.class));
    }

    @Test
    @DisplayName("REST-CURSO-05: DELETE /api/cursos/{id} elimina curso")
    void testEliminarCurso() throws Exception {
        mockMvc.perform(delete("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Curso eliminado correctamente")));

        verify(cursoService).eliminarCurso(1);
    }
}
