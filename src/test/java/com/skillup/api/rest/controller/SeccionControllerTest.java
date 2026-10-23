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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.service.SeccionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SeccionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SeccionService seccionService;

    @InjectMocks
    private SeccionController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-SEC-01: GET /api/secciones/curso/{idCurso} retorna secciones de curso")
    void testListarPorCurso() throws Exception {
        Seccion sec = new Seccion();
        sec.setIdSeccion(1);
        sec.setIdCurso(10);
        sec.setNombreSeccion("Módulo 1: Fundamentos");

        when(seccionService.listarSeccionesPorCurso(10)).thenReturn(List.of(sec));

        mockMvc.perform(get("/api/secciones/curso/{idCurso}", 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idSeccion").value(1))
                .andExpect(jsonPath("$[0].nombreSeccion").value("Módulo 1: Fundamentos"));

        verify(seccionService, times(1)).listarSeccionesPorCurso(10);
    }

    @Test
    @DisplayName("REST-SEC-02: GET /api/secciones/seccion/{id} retorna sección por ID")
    void testObtenerPorId() throws Exception {
        Seccion sec = new Seccion();
        sec.setIdSeccion(5);
        sec.setNombreSeccion("Módulo Avanzado");

        when(seccionService.obtenerSeccionPorId(5)).thenReturn(sec);

        mockMvc.perform(get("/api/secciones/seccion/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSeccion").value(5));

        verify(seccionService, times(1)).obtenerSeccionPorId(5);
    }

    @Test
    @DisplayName("REST-SEC-03: POST /api/secciones inserta sección mediante servicio")
    void testInsertarSeccion() throws Exception {
        Seccion sec = new Seccion();
        sec.setIdCurso(10);
        sec.setNombreSeccion("Módulo Nuevo");

        doNothing().when(seccionService).insertarSeccion(any(Seccion.class));

        mockMvc.perform(post("/api/secciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sec)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("registrada correctamente")));

        verify(seccionService, times(1)).insertarSeccion(any(Seccion.class));
    }

    @Test
    @DisplayName("REST-SEC-04: DELETE /api/secciones/seccion/{id} elimina sección")
    void testEliminarSeccion() throws Exception {
        doNothing().when(seccionService).eliminarSeccion(3);

        mockMvc.perform(delete("/api/secciones/seccion/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("eliminada correctamente")));

        verify(seccionService, times(1)).eliminarSeccion(3);
    }
}
