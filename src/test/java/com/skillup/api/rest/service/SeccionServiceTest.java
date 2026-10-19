package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.repository.SeccionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class SeccionServiceTest {

    @Mock
    private SeccionRepository seccionRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private SeccionService seccionService;

    private Seccion seccionPrueba;

    @BeforeEach
    void setUp() {
        seccionPrueba = new Seccion();
        seccionPrueba.setIdSeccion(10);
        seccionPrueba.setIdCurso(1);
        seccionPrueba.setNombreSeccion("Módulo 1: Fundamentos de Arquitectura");
        seccionPrueba.setOrdenSeccion(1);
        seccionPrueba.setEstado(1);
    }

    @Test
    @DisplayName("CP-SECCION-01: Listar secciones por curso ejecuta SP_ListarSeccionesporCurso")
    void testListarSeccionesPorCurso() {
        when(entityManager.createStoredProcedureQuery("SP_ListarSeccionesporCurso", Seccion.class))
            .thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.singletonList(seccionPrueba));

        List<Seccion> resultado = seccionService.listarSeccionesPorCurso(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Módulo 1: Fundamentos de Arquitectura", resultado.get(0).getNombreSeccion());
        verify(storedProcedureQuery).setParameter("idCurso", 1);
    }

    @Test
    @DisplayName("CP-SECCION-02: Obtener sección por ID existente retorna la entidad")
    void testObtenerSeccionPorId_Existente() {
        when(seccionRepository.findById(10)).thenReturn(Optional.of(seccionPrueba));

        Seccion resultado = seccionService.obtenerSeccionPorId(10);

        assertNotNull(resultado);
        assertEquals(10, resultado.getIdSeccion());
        assertEquals("Módulo 1: Fundamentos de Arquitectura", resultado.getNombreSeccion());
        verify(seccionRepository).findById(10);
    }

    @Test
    @DisplayName("CP-SECCION-03: Obtener sección por ID inexistente retorna null")
    void testObtenerSeccionPorId_NoExistente() {
        when(seccionRepository.findById(999)).thenReturn(Optional.empty());

        Seccion resultado = seccionService.obtenerSeccionPorId(999);

        assertNull(resultado);
        verify(seccionRepository).findById(999);
    }

    @Test
    @DisplayName("CP-SECCION-04: Insertar sección delega al SP_InsertSeccion")
    void testInsertarSeccion() {
        when(entityManager.createStoredProcedureQuery("SP_InsertSeccion")).thenReturn(storedProcedureQuery);

        seccionService.insertarSeccion(seccionPrueba);

        verify(entityManager).createStoredProcedureQuery("SP_InsertSeccion");
        verify(storedProcedureQuery).setParameter("Id_Curso", 1);
        verify(storedProcedureQuery).setParameter("Nombre_Seccion", "Módulo 1: Fundamentos de Arquitectura");
        verify(storedProcedureQuery).setParameter("Orden_Seccion", 1);
        verify(storedProcedureQuery).execute();
    }

    @Test
    @DisplayName("CP-SECCION-05: Actualizar sección delega al SP_UpdateSeccionPorID")
    void testActualizarSeccion() {
        when(entityManager.createStoredProcedureQuery("SP_UpdateSeccionPorID")).thenReturn(storedProcedureQuery);

        seccionService.actualizarSeccion(seccionPrueba);

        verify(entityManager).createStoredProcedureQuery("SP_UpdateSeccionPorID");
        verify(storedProcedureQuery).setParameter("Id_Seccion", 10);
        verify(storedProcedureQuery).setParameter("Estado", 1);
        verify(storedProcedureQuery).execute();
    }

    @Test
    @DisplayName("CP-SECCION-06: Eliminar sección ejecuta SP_DeleteSeccionPorID con ID de sección")
    void testEliminarSeccion() {
        when(entityManager.createStoredProcedureQuery("SP_DeleteSeccionPorID")).thenReturn(storedProcedureQuery);

        seccionService.eliminarSeccion(10);

        verify(entityManager).createStoredProcedureQuery("SP_DeleteSeccionPorID");
        verify(storedProcedureQuery).setParameter("Id_Seccion", 10);
        verify(storedProcedureQuery).execute();
    }
}
