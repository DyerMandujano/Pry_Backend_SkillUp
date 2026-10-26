package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.model.Material;
import com.skillup.api.rest.repository.LeccionReporsitory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class LeccionServiceTest {

    @Mock
    private LeccionReporsitory leccionRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private LeccionService leccionService;

    private Leccion leccionPrueba;

    @BeforeEach
    void setUp() {
        leccionPrueba = new Leccion();
        leccionPrueba.setIdLeccion(100);
        leccionPrueba.setIdSeccion(10);
        leccionPrueba.setNombreLeccion("1.1 Introducción a Spring Boot 3");
        leccionPrueba.setDuracion(45);
        leccionPrueba.setOrdenLeccion(1);
        leccionPrueba.setEstado(1);
        leccionPrueba.setUrlVideo("https://video.skillup.pe/leccion1");
        leccionPrueba.setMateriales(new ArrayList<>());
    }

    @Test
    @DisplayName("CP-LECCION-01: Listar lecciones por sección delega al SP_ListarLeccionesporSeccion")
    void testListarLeccionesPorSeccion() {
        when(entityManager.createStoredProcedureQuery("SP_ListarLeccionesporSeccion", Leccion.class))
            .thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.singletonList(leccionPrueba));

        List<Leccion> resultado = leccionService.listarLeccionesPorSeccion(10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("1.1 Introducción a Spring Boot 3", resultado.get(0).getNombreLeccion());
        verify(storedProcedureQuery).setParameter("id_seccion", 10);
    }

    @Test
    @DisplayName("CP-LECCION-02: Obtener lección por ID existente retorna la entidad")
    void testObtenerLeccionPorId_Existente() {
        when(leccionRepository.findById(100)).thenReturn(Optional.of(leccionPrueba));

        Leccion resultado = leccionService.obtenerLeccionPorId(100);

        assertNotNull(resultado);
        assertEquals(100, resultado.getIdLeccion());
        assertEquals("1.1 Introducción a Spring Boot 3", resultado.getNombreLeccion());
        verify(leccionRepository).findById(100);
    }

    @Test
    @DisplayName("CP-LECCION-03: Obtener lección por ID inexistente retorna null")
    void testObtenerLeccionPorId_NoExistente() {
        when(leccionRepository.findById(999)).thenReturn(Optional.empty());

        Leccion resultado = leccionService.obtenerLeccionPorId(999);

        assertNull(resultado);
        verify(leccionRepository).findById(999);
    }

    @Test
    @DisplayName("CP-LECCION-04: Insertar lección enlaza materiales y guarda en JPA")
    void testInsertarLeccion_ConMateriales() {
        Material material = new Material();
        material.setUrlMaterial("https://materiales.skillup.pe/guia.pdf");
        material.setNombreArchivo("Guia_Spring.pdf");

        leccionPrueba.setMateriales(Collections.singletonList(material));

        leccionService.insertarLeccion(leccionPrueba);

        assertEquals(leccionPrueba, material.getLeccion());
        verify(leccionRepository).save(leccionPrueba);
    }

    @Test
    @DisplayName("CP-LECCION-05: Actualizar lección modifica datos y guarda cambios")
    void testActualizarLeccion_ExistenteActualizaCampos() {
        when(leccionRepository.findById(100)).thenReturn(Optional.of(leccionPrueba));

        Leccion leccionActualizada = new Leccion();
        leccionActualizada.setIdLeccion(100);
        leccionActualizada.setNombreLeccion("1.1 Introducción Avanzada a Spring Boot");
        leccionActualizada.setDuracion(60);
        leccionActualizada.setOrdenLeccion(1);
        leccionActualizada.setEstado(1);
        leccionActualizada.setUrlVideo("https://video.skillup.pe/leccion1_v2");
        leccionActualizada.setMateriales(new ArrayList<>());

        leccionService.actualizarLeccion(leccionActualizada);

        assertEquals("1.1 Introducción Avanzada a Spring Boot", leccionPrueba.getNombreLeccion());
        assertEquals(60, leccionPrueba.getDuracion());
        verify(leccionRepository).save(leccionPrueba);
    }
}
