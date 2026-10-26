package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.MatriculaSeguimientoDTO;
import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Matricula;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.repository.MatriculaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
public class AdminMatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Matricula> matriculaQuery;

    @Mock
    private TypedQuery<Usuario> usuarioQuery;

    @Mock
    private TypedQuery<Long> totalLeccionesQuery;

    @InjectMocks
    private AdminMatriculaService adminMatriculaService;

    private Matricula matricula;
    private Curso curso;
    private Estudiante estudiante;
    private Persona persona;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        matricula = new Matricula();
        matricula.setIdMatricula(1);
        matricula.setIdEstudiante(10);
        matricula.setIdCurso(20);
        matricula.setFechaMatricula(new Date(System.currentTimeMillis()));
        matricula.setEstado(1); // Activo

        curso = new Curso();
        curso.setIdCurso(20);
        curso.setNombreCurso("Java Profesional");

        estudiante = new Estudiante();
        estudiante.setIdEstudiante(10);
        estudiante.setIdPersona(100);

        persona = new Persona();
        persona.setIdPersona(100);
        persona.setNombres("Juan");
        persona.setApellidos("Pérez");
        persona.setDni("72345678");

        usuario = new Usuario();
        usuario.setUsername("juanperez");
    }

    @Test
    @DisplayName("CP-ADM-MAT-01: Listar seguimiento de matrículas mapea datos de estudiante y curso")
    void testListarMatriculasSeguimiento_SinFiltro() {
        when(entityManager.createQuery(anyString(), eq(Matricula.class))).thenReturn(matriculaQuery);
        when(matriculaQuery.getResultList()).thenReturn(Collections.singletonList(matricula));

        when(entityManager.find(Curso.class, 20)).thenReturn(curso);
        when(entityManager.find(Estudiante.class, 10)).thenReturn(estudiante);
        when(entityManager.find(Persona.class, 100)).thenReturn(persona);

        when(entityManager.createQuery("SELECT u FROM Usuario u WHERE u.idPersona = :idp", Usuario.class)).thenReturn(usuarioQuery);
        when(usuarioQuery.getResultList()).thenReturn(Collections.singletonList(usuario));

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(totalLeccionesQuery);
        when(totalLeccionesQuery.getSingleResult()).thenReturn(10L);

        List<MatriculaSeguimientoDTO> resultado = adminMatriculaService.listarMatriculasSeguimiento(null, null);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Java Profesional", resultado.get(0).getNombreCurso());
        assertEquals("Juan Pérez", resultado.get(0).getEstudianteNombre());
        assertEquals("72345678", resultado.get(0).getEstudianteDni());
        assertEquals("juanperez", resultado.get(0).getEstudianteUsername());
        assertEquals("Activo", resultado.get(0).getEstadoDescripcion());
    }

    @Test
    @DisplayName("CP-ADM-MAT-02: Filtro por texto excluye matrículas que no coinciden con la búsqueda")
    void testListarMatriculasSeguimiento_ConFiltroNoCoincidente() {
        when(entityManager.createQuery(anyString(), eq(Matricula.class))).thenReturn(matriculaQuery);
        when(matriculaQuery.getResultList()).thenReturn(Collections.singletonList(matricula));

        when(entityManager.find(Curso.class, 20)).thenReturn(curso);
        when(entityManager.find(Estudiante.class, 10)).thenReturn(estudiante);
        when(entityManager.find(Persona.class, 100)).thenReturn(persona);

        when(entityManager.createQuery("SELECT u FROM Usuario u WHERE u.idPersona = :idp", Usuario.class)).thenReturn(usuarioQuery);
        when(usuarioQuery.getResultList()).thenReturn(Collections.singletonList(usuario));

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(totalLeccionesQuery);
        when(totalLeccionesQuery.getSingleResult()).thenReturn(10L);

        List<MatriculaSeguimientoDTO> resultado = adminMatriculaService.listarMatriculasSeguimiento(null, "Python");

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    @DisplayName("CP-ADM-MAT-03: Matrícula finalizada (estado 2) calcula avance al 100%")
    void testListarMatriculasSeguimiento_EstadoFinalizado() {
        matricula.setEstado(2);

        when(entityManager.createQuery(anyString(), eq(Matricula.class))).thenReturn(matriculaQuery);
        when(matriculaQuery.getResultList()).thenReturn(Collections.singletonList(matricula));

        when(entityManager.find(Curso.class, 20)).thenReturn(curso);
        when(entityManager.find(Estudiante.class, 10)).thenReturn(estudiante);
        when(entityManager.find(Persona.class, 100)).thenReturn(persona);

        when(entityManager.createQuery("SELECT u FROM Usuario u WHERE u.idPersona = :idp", Usuario.class)).thenReturn(usuarioQuery);
        when(usuarioQuery.getResultList()).thenReturn(Collections.singletonList(usuario));

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(totalLeccionesQuery);
        when(totalLeccionesQuery.getSingleResult()).thenReturn(5L);

        List<MatriculaSeguimientoDTO> resultado = adminMatriculaService.listarMatriculasSeguimiento(20, null);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Finalizado", resultado.get(0).getEstadoDescripcion());
        assertEquals(100, resultado.get(0).getPorcentajeAvance());
    }
}
