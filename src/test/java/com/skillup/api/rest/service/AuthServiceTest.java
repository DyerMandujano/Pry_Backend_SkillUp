package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.RegistroRequest;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private AuthService authService;

    private RegistroRequest requestValido;

    @BeforeEach
    void setUp() {
        Persona persona = new Persona();
        persona.setNombres("Carlos");
        persona.setApellidos("Mendoza");
        persona.setDni("76543210"); // DNI válido 8 dígitos
        persona.setGenero("Masculino");

        Usuario usuario = new Usuario();
        usuario.setUsername("cmendoza");
        usuario.setContrasenia("123456"); // Máximo 6 caracteres
        usuario.setRol("estudiante");

        Estudiante estudiante = new Estudiante();
        estudiante.setNivelEducativo("Superior");

        requestValido = new RegistroRequest();
        requestValido.setPersona(persona);
        requestValido.setUsuario(usuario);
        requestValido.setEstudiante(estudiante);
    }

    @Test
    @DisplayName("CP-01: Registro exitoso con DNI de 8 dígitos y contraseña <= 6 caracteres")
    void testRegistroExitoso() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(eq("username"), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        authService.registrarUsuario(requestValido);

        verify(entityManager).persist(any(Persona.class));
        verify(entityManager).persist(any(Usuario.class));
        verify(entityManager).persist(any(Estudiante.class));
    }

    @Test
    @DisplayName("CP-02 [Negativo]: Falla si el DNI no tiene exactamente 8 dígitos numéricos")
    void testRegistroDniInvalido() {
        requestValido.getPersona().setDni("12345"); // Menos de 8 dígitos

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("DNI es obligatorio y debe contener exactamente 8 dígitos numéricos"));
    }

    @Test
    @DisplayName("CP-03 [Negativo]: Falla si la contraseña supera los 6 dígitos")
    void testRegistroContraseniaExcedeLongitud() {
        requestValido.getPersona().setDni("87654321");
        requestValido.getUsuario().setContrasenia("12345678"); // 8 caracteres (> 6)

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("máximo 6 dígitos"));
    }

    @Test
    @DisplayName("CP-04 [Seguridad]: Bloquea auto-asignación de rol admin en registro público")
    void testRegistroPublicoBloqueaRolAdmin() {
        requestValido.getUsuario().setRol("admin");

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(eq("username"), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("No está autorizado a registrar cuentas de administrador"));
    }

    @Test
    @DisplayName("CP-05 [CRUD Admin]: Creación exitosa de admin si el ejecutor tiene rol ADMIN")
    void testRegistrarAdminConRolAdmin() {
        requestValido.getUsuario().setRol("admin");

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(eq("username"), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        authService.registrarAdmin(requestValido, "admin");

        assertEquals("admin", requestValido.getUsuario().getRol());
        verify(entityManager).persist(any(Persona.class));
        verify(entityManager).persist(any(Usuario.class));
    }

    @Test
    @DisplayName("CP-06 [Seguridad]: Bloquea creación de admin si el ejecutor NO es ADMIN")
    void testRegistrarAdminConRolInvalido() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarAdmin(requestValido, "estudiante");
        });

        assertTrue(ex.getMessage().contains("solo un usuario con rol ADMIN puede crear otro usuario ADMIN"));
    }
}
