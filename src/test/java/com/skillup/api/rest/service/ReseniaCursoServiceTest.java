package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.RegistrarReseniaRequestDTO;
import com.skillup.api.rest.dtos.ReseniaCursoDTO;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.ReseniaCurso;
import com.skillup.api.rest.repository.EstudianteRepository;
import com.skillup.api.rest.repository.MatriculaRepository;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.repository.ReseniaCursoRepository;

@ExtendWith(MockitoExtension.class)
public class ReseniaCursoServiceTest {

    @Mock
    private ReseniaCursoRepository reseniaCursoRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private ReseniaCursoService reseniaCursoService;

    private Estudiante estudianteMock;
    private Persona personaMock;

    @BeforeEach
    void setUp() {
        estudianteMock = new Estudiante();
        estudianteMock.setIdEstudiante(1);
        estudianteMock.setIdPersona(10);

        personaMock = new Persona();
        personaMock.setIdPersona(10);
        personaMock.setNombres("Juan");
        personaMock.setApellidos("Pérez");
    }

    @Test
    @DisplayName("HU20 - Escenario 1: Estudiante matriculado publica reseña con 5 estrellas exitosamente")
    void testRegistrarResenia_Exitoso() {
        int idCurso = 1;
        RegistrarReseniaRequestDTO request = new RegistrarReseniaRequestDTO(1, 5, "Excelente curso, muy práctico.");

        when(matriculaRepository.existsByIdEstudianteAndIdCurso(1, idCurso)).thenReturn(true);

        ReseniaCurso guardada = new ReseniaCurso(100, idCurso, 1, "Excelente curso, muy práctico.", 5, new Date(), 1);
        when(reseniaCursoRepository.save(any(ReseniaCurso.class))).thenReturn(guardada);
        when(estudianteRepository.findById(1)).thenReturn(Optional.of(estudianteMock));
        when(personaRepository.findById(10)).thenReturn(Optional.of(personaMock));

        ReseniaCursoDTO resultado = reseniaCursoService.registrarResenia(idCurso, request);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());
        assertEquals("Excelente curso, muy práctico.", resultado.getComentario());
        assertEquals("Juan Pérez", resultado.getNombreEstudiante());
        verify(reseniaCursoRepository).save(any(ReseniaCurso.class));
    }

    @Test
    @DisplayName("HU20 - Escenario 2: Intento de registro sin calificación o fuera de rango (1-5) es rechazado")
    void testRegistrarResenia_SinCalificacion() {
        int idCurso = 1;
        RegistrarReseniaRequestDTO requestSinEstrellas = new RegistrarReseniaRequestDTO(1, null, "Buen curso.");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reseniaCursoService.registrarResenia(idCurso, requestSinEstrellas);
        });

        assertTrue(ex.getMessage().contains("La calificación es obligatoria"));

        RegistrarReseniaRequestDTO requestCalificacionInvalida = new RegistrarReseniaRequestDTO(1, 6, "Buen curso.");
        assertThrows(RuntimeException.class, () -> {
            reseniaCursoService.registrarResenia(idCurso, requestCalificacionInvalida);
        });
    }

    @Test
    @DisplayName("HU20 - Escenario 3: Estudiante sin matrícula intenta comentar y es bloqueado")
    void testRegistrarResenia_SinMatricula() {
        int idCurso = 1;
        RegistrarReseniaRequestDTO request = new RegistrarReseniaRequestDTO(99, 4, "Quiero opinar.");

        when(matriculaRepository.existsByIdEstudianteAndIdCurso(99, idCurso)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reseniaCursoService.registrarResenia(idCurso, request);
        });

        assertTrue(ex.getMessage().contains("Solo los estudiantes matriculados"));
    }
}
