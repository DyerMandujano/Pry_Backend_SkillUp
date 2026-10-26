package com.skillup.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

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
import com.skillup.api.rest.dtos.LoginRequest;
import com.skillup.api.rest.dtos.LoginResponse;
import com.skillup.api.rest.dtos.RegistroRequest;
import com.skillup.api.rest.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("REST-AUTH-01: POST /api/auth/login exitoso retorna 200 OK y token")
    void testLogin_Exitoso() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("juan123");
        req.setContrasenia("123456");

        LoginResponse resp = new LoginResponse(
            "juan123",
            "estudiante",
            "Juan Pérez",
            "jwt-sample-token",
            1,
            20,
            10
        );

        when(authService.validarLogin(any(LoginRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-sample-token"))
                .andExpect(jsonPath("$.username").value("juan123"))
                .andExpect(jsonPath("$.rol").value("estudiante"));
    }

    @Test
    @DisplayName("REST-AUTH-02 [Negativo]: POST /api/auth/login con credenciales inválidas retorna 401 Unauthorized")
    void testLogin_Invalido() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("usuario_invalido");
        req.setContrasenia("clave_incorrecta");

        when(authService.validarLogin(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Credenciales incorrectas"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciales incorrectas"));
    }

    @Test
    @DisplayName("REST-AUTH-03: POST /api/auth/registrar exitoso retorna 200 OK")
    void testRegistrar_Exitoso() throws Exception {
        RegistroRequest req = new RegistroRequest();

        mockMvc.perform(post("/api/auth/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado exitosamente"));
    }

    @Test
    @DisplayName("REST-AUTH-04 [Negativo]: POST /api/auth/registrar con error de negocio retorna 400 Bad Request")
    void testRegistrar_FallaNegocio() throws Exception {
        RegistroRequest req = new RegistroRequest();

        doThrow(new RuntimeException("El DNI ya se encuentra registrado")).when(authService).registrarUsuario(any(RegistroRequest.class));

        mockMvc.perform(post("/api/auth/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El DNI ya se encuentra registrado"));
    }

    @Test
    @DisplayName("REST-AUTH-05: POST /api/auth/registrar-admin sin permisos retorna 403 Forbidden")
    void testRegistrarAdmin_SinPermiso() throws Exception {
        RegistroRequest req = new RegistroRequest();

        doThrow(new RuntimeException("Solo un administrador puede ejecutar esta acción"))
                .when(authService).registrarAdmin(any(RegistroRequest.class), eq("estudiante"));

        mockMvc.perform(post("/api/auth/registrar-admin")
                .header("X-User-Role", "estudiante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Solo un administrador puede ejecutar esta acción"));
    }

    @Test
    @DisplayName("REST-AUTH-06: POST /api/auth/recuperar-password/verificar retorna datos del usuario")
    void testVerificarIdentificador_Exitoso() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("identificador", "72345678");

        Map<String, Object> detalle = new HashMap<>();
        detalle.put("username", "carlos_g");

        when(authService.verificarIdentificadorDetallado("72345678")).thenReturn(detalle);

        mockMvc.perform(post("/api/auth/recuperar-password/verificar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("carlos_g"))
                .andExpect(jsonPath("$.mensaje").value("Usuario verificado exitosamente"));
    }
}
