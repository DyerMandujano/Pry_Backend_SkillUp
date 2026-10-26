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
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.service.UsuarioService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Test
    @DisplayName("REST-USR-01: GET /api/usuarios/perfil/{id} retorna perfil de persona")
    void testObtenerPerfil() throws Exception {
        Persona persona = new Persona();
        persona.setIdPersona(1);
        persona.setNombres("Juan");
        persona.setApellidos("Pérez");

        when(usuarioService.obtenerPerfil(1)).thenReturn(persona);

        mockMvc.perform(get("/api/usuarios/perfil/{idPersona}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersona").value(1))
                .andExpect(jsonPath("$.nombres").value("Juan"))
                .andExpect(jsonPath("$.apellidos").value("Pérez"));

        verify(usuarioService, times(1)).obtenerPerfil(1);
    }

    @Test
    @DisplayName("REST-USR-02: PUT /api/usuarios/perfil/{id} actualiza perfil exitosamente")
    void testActualizarPerfil() throws Exception {
        Persona persona = new Persona();
        persona.setNombres("Carlos");
        persona.setApellidos("Ramírez");

        doNothing().when(usuarioService).actualizarPerfil(eq(1), any(Persona.class));

        mockMvc.perform(put("/api/usuarios/perfil/{idPersona}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(persona)))
                .andExpect(status().isOk())
                .andExpect(content().string("Perfil actualizado correctamente"));

        verify(usuarioService, times(1)).actualizarPerfil(eq(1), any(Persona.class));
    }

    @Test
    @DisplayName("REST-USR-03: GET /api/usuarios/listar-personas retorna lista de personas")
    void testListarPersonas() throws Exception {
        Persona persona = new Persona();
        persona.setIdPersona(2);
        when(usuarioService.listarTodasLasPersonas()).thenReturn(List.of(persona));

        mockMvc.perform(get("/api/usuarios/listar-personas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPersona").value(2));

        verify(usuarioService, times(1)).listarTodasLasPersonas();
    }
}
