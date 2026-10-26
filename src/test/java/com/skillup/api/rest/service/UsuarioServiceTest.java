package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.repository.PersonaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private UsuarioService usuarioService;

    private Persona persona;

    @BeforeEach
    void setUp() {
        persona = new Persona();
        persona.setIdPersona(10);
        persona.setNombres("Carlos");
        persona.setApellidos("García");
        persona.setDni("87654321");
        persona.setFechaDeNacimiento(new Date());
        persona.setGenero("M");
        persona.setEstado(1);
    }

    @Test
    @DisplayName("CP-USUARIO-01: Obtener perfil ejecuta SP_BuscarPersonaPorID y retorna persona")
    @SuppressWarnings("unchecked")
    void testObtenerPerfil_Exitoso() {
        when(entityManager.createStoredProcedureQuery("dbo.SP_BuscarPersonaPorID", Persona.class))
                .thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.singletonList(persona));

        Persona resultado = usuarioService.obtenerPerfil(10);

        assertNotNull(resultado);
        assertEquals(10, resultado.getIdPersona());
        assertEquals("Carlos", resultado.getNombres());

        verify(storedProcedureQuery).registerStoredProcedureParameter("id", Integer.class, ParameterMode.IN);
        verify(storedProcedureQuery).setParameter("id", 10);
    }

    @Test
    @DisplayName("CP-USUARIO-02 [Negativo]: Obtener perfil con ID inexistente lanza RuntimeException")
    @SuppressWarnings("unchecked")
    void testObtenerPerfil_NoEncontrado() {
        when(entityManager.createStoredProcedureQuery("dbo.SP_BuscarPersonaPorID", Persona.class))
                .thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPerfil(999);
        });
    }

    @Test
    @DisplayName("CP-USUARIO-03: Actualizar perfil ejecuta SP_UpdatePersonaPorID")
    void testActualizarPerfil_Exitoso() {
        when(entityManager.createStoredProcedureQuery("dbo.SP_UpdatePersonaPorID"))
                .thenReturn(storedProcedureQuery);

        usuarioService.actualizarPerfil(10, persona);

        verify(storedProcedureQuery).setParameter("id", 10);
        verify(storedProcedureQuery).setParameter("nombres", "Carlos");
        verify(storedProcedureQuery).setParameter("apellidos", "García");
        verify(storedProcedureQuery).execute();
    }

    @Test
    @DisplayName("CP-USUARIO-04 [Negativo]: Actualizar perfil con nombre vacío lanza RuntimeException")
    void testActualizarPerfil_NombreVacio() {
        persona.setNombres("");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarPerfil(10, persona);
        });
        assertEquals("El nombre no puede estar vacío", ex.getMessage());
    }

    @Test
    @DisplayName("CP-USUARIO-05: Listar todas las personas ejecuta SP_ListarPersonas")
    @SuppressWarnings("unchecked")
    void testListarTodasLasPersonas() {
        when(entityManager.createStoredProcedureQuery("dbo.SP_ListarPersonas", Persona.class))
                .thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.singletonList(persona));

        List<Persona> resultado = usuarioService.listarTodasLasPersonas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNombres());
    }
}
