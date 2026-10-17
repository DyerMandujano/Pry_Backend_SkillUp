package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.UsuarioAdminDTO;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.repository.UsuarioRepository;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class AdminUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AdminUsuarioService adminUsuarioService;

    private Usuario usuarioPrueba;
    private Persona personaPrueba;

    @BeforeEach
    void setUp() {
        personaPrueba = new Persona();
        personaPrueba.setIdPersona(100);
        personaPrueba.setNombres("Maria");
        personaPrueba.setApellidos("Santos");
        personaPrueba.setDni("87654321");
        personaPrueba.setEstado(1);

        usuarioPrueba = new Usuario();
        usuarioPrueba.setIdUsuario(200);
        usuarioPrueba.setIdPersona(100);
        usuarioPrueba.setUsername("msantos");
        usuarioPrueba.setRol("estudiante");
    }

    @Test
    @DisplayName("HU18 Escenario 1: Desactivar cuenta aplica baja lógica (estado = 0)")
    void testDesactivarCuentaBajaLogica() {
        when(usuarioRepository.findById(200)).thenReturn(Optional.of(usuarioPrueba));
        when(entityManager.find(Persona.class, 100)).thenReturn(personaPrueba);

        UsuarioAdminDTO resultado = adminUsuarioService.cambiarEstadoUsuario(200, 0);

        assertNotNull(resultado);
        assertEquals(0, resultado.getEstado());
        assertEquals(0, personaPrueba.getEstado());
        verify(entityManager).merge(personaPrueba);
    }

    @Test
    @DisplayName("HU18 Escenario 2: Reactivar cuenta restablece permisos (estado = 1)")
    void testReactivarCuenta() {
        personaPrueba.setEstado(0); // Previamente inactivo

        when(usuarioRepository.findById(200)).thenReturn(Optional.of(usuarioPrueba));
        when(entityManager.find(Persona.class, 100)).thenReturn(personaPrueba);

        UsuarioAdminDTO resultado = adminUsuarioService.cambiarEstadoUsuario(200, 1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getEstado());
        assertEquals(1, personaPrueba.getEstado());
        verify(entityManager).merge(personaPrueba);
    }

    @Test
    @DisplayName("HU18: Usuario inexistente arroja excepción descriptiva")
    void testUsuarioInexistente() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            adminUsuarioService.cambiarEstadoUsuario(999, 0);
        });
    }
}
