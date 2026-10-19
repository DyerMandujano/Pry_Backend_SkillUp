package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.repository.CursoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private CursoService cursoService;

    private Curso cursoPrueba;

    @BeforeEach
    void setUp() {
        cursoPrueba = new Curso();
        cursoPrueba.setIdCurso(1);
        cursoPrueba.setIdDocente(5);
        cursoPrueba.setIdCategoria(2);
        cursoPrueba.setNombreCurso("Java Spring Boot Avanzado");
        cursoPrueba.setDescripcion("Curso completo de arquitectura hexagonal y microservicios.");
        cursoPrueba.setResumen("Aprende Spring Boot desde cero a producción.");
        cursoPrueba.setNivel("Intermedio");
        cursoPrueba.setDuracionMin(120);
        cursoPrueba.setImagenCurso1("img1.png");
        cursoPrueba.setImagenCurso2("img2.png");
        cursoPrueba.setEstado(1);
    }

    @Test
    @DisplayName("CP-CURSO-01: Listar cursos retorna lista completa de la BD")
    void testListarCursos_RetornaLista() {
        Curso curso2 = new Curso();
        curso2.setIdCurso(2);
        curso2.setNombreCurso("Angular 19 Frontend Profesional");

        when(cursoRepository.findAll()).thenReturn(Arrays.asList(cursoPrueba, curso2));

        List<Curso> resultado = cursoService.listarCursos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Java Spring Boot Avanzado", resultado.get(0).getNombreCurso());
        verify(cursoRepository).findAll();
    }

    @Test
    @DisplayName("CP-CURSO-02: Obtener curso por ID existente retorna la entidad correspondiente")
    void testObtenerCursoPorId_Existente() {
        when(cursoRepository.findById(1)).thenReturn(Optional.of(cursoPrueba));

        Curso resultado = cursoService.obtenerCursoPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCurso());
        assertEquals("Java Spring Boot Avanzado", resultado.getNombreCurso());
        verify(cursoRepository).findById(1);
    }

    @Test
    @DisplayName("CP-CURSO-03: Obtener curso por ID inexistente retorna null")
    void testObtenerCursoPorId_NoExistente() {
        when(cursoRepository.findById(999)).thenReturn(Optional.empty());

        Curso resultado = cursoService.obtenerCursoPorId(999);

        assertNull(resultado);
        verify(cursoRepository).findById(999);
    }

    @Test
    @DisplayName("CP-CURSO-04: Insertar curso delega correctamente al SP_InsertCurso")
    void testInsertarCurso() {
        when(entityManager.createStoredProcedureQuery("SP_InsertCurso")).thenReturn(storedProcedureQuery);

        cursoService.insertarCurso(cursoPrueba);

        verify(entityManager).createStoredProcedureQuery("SP_InsertCurso");
        verify(storedProcedureQuery).setParameter("Id_Docente", cursoPrueba.getIdDocente());
        verify(storedProcedureQuery).setParameter("Nombre_Curso", cursoPrueba.getNombreCurso());
        verify(storedProcedureQuery).execute();
    }

    @Test
    @DisplayName("CP-CURSO-05: Actualizar curso delega al SP_UpdateCursoPorID")
    void testActualizarCurso() {
        when(entityManager.createStoredProcedureQuery("SP_UpdateCursoPorID")).thenReturn(storedProcedureQuery);

        cursoService.actualizarCurso(cursoPrueba);

        verify(entityManager).createStoredProcedureQuery("SP_UpdateCursoPorID");
        verify(storedProcedureQuery).setParameter("Id_Curso", cursoPrueba.getIdCurso());
        verify(storedProcedureQuery).setParameter("Estado", cursoPrueba.getEstado());
        verify(storedProcedureQuery).execute();
    }

    @Test
    @DisplayName("CP-CURSO-06: Eliminar curso ejecuta SP_DeleteCursoPorID con ID de curso")
    void testEliminarCurso() {
        when(entityManager.createStoredProcedureQuery("SP_DeleteCursoPorID")).thenReturn(storedProcedureQuery);

        cursoService.eliminarCurso(1);

        verify(entityManager).createStoredProcedureQuery("SP_DeleteCursoPorID");
        verify(storedProcedureQuery).setParameter("id", 1);
        verify(storedProcedureQuery).execute();
    }
}
