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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.service.LeccionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LeccionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LeccionService leccionService;

    @InjectMocks
    private LeccionController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-LEC-01: GET /api/lecciones/seccion/{idSeccion} retorna lecciones")
    void testListarPorSeccion() throws Exception {
        Leccion lec = new Leccion();
        lec.setIdLeccion(1);
        lec.setNombreLeccion("Introducción");

        when(leccionService.listarLeccionesPorSeccion(5)).thenReturn(List.of(lec));

        mockMvc.perform(get("/api/lecciones/seccion/{idSeccion}", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idLeccion").value(1))
                .andExpect(jsonPath("$[0].nombreLeccion").value("Introducción"));

        verify(leccionService, times(1)).listarLeccionesPorSeccion(5);
    }

    @Test
    @DisplayName("REST-LEC-02: GET /api/lecciones/{id} retorna lección si existe o 404 si no")
    void testObtenerPorId() throws Exception {
        Leccion lec = new Leccion();
        lec.setIdLeccion(10);
        lec.setNombreLeccion("Variables y Tipos");

        when(leccionService.obtenerLeccionPorId(10)).thenReturn(lec);
        when(leccionService.obtenerLeccionPorId(99)).thenReturn(null);

        mockMvc.perform(get("/api/lecciones/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLeccion").value(10));

        mockMvc.perform(get("/api/lecciones/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("REST-LEC-03: POST /api/lecciones crea lección con datos multipart")
    void testInsertar() throws Exception {
        MockMultipartFile leccionPart = new MockMultipartFile(
                "leccion",
                "",
                "application/json",
                "{\"nombreLeccion\":\"POO en Java\",\"idSeccion\":2}".getBytes()
        );

        doNothing().when(leccionService).insertarLeccion(any(Leccion.class));

        mockMvc.perform(multipart("/api/lecciones")
                .file(leccionPart))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Lección creada correctamente")));

        verify(leccionService, times(1)).insertarLeccion(any(Leccion.class));
    }

    @Test
    @DisplayName("REST-LEC-04: DELETE /api/lecciones/{id} elimina lección")
    void testEliminar() throws Exception {
        doNothing().when(leccionService).eliminarLeccion(1);

        mockMvc.perform(delete("/api/lecciones/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Lección eliminada correctamente")));

        verify(leccionService, times(1)).eliminarLeccion(1);
    }
}
