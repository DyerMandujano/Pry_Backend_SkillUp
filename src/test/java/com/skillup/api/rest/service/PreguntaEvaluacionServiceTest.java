package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.model.PreguntaEvaluacion;
import com.skillup.api.rest.repository.EvaluacionRepository;
import com.skillup.api.rest.repository.PreguntaEvaluacionRepository;

@ExtendWith(MockitoExtension.class)
public class PreguntaEvaluacionServiceTest {

    @Mock
    private PreguntaEvaluacionRepository preguntaEvaluacionRepository;

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @InjectMocks
    private PreguntaEvaluacionService preguntaEvaluacionService;

    private PreguntaEvaluacion pregunta;
    private Evaluacion evaluacion;

    @BeforeEach
    void setUp() {
        evaluacion = new Evaluacion();
        evaluacion.setIdEvaluacion(1);
        evaluacion.setTitulo("Examen Final");

        pregunta = new PreguntaEvaluacion();
        pregunta.setIdPregunta(10);
        pregunta.setEvaluacion(evaluacion);
        pregunta.setTipoPregunta("Multiple");
        pregunta.setEnunciado("¿Qué es polimorfismo?");
        pregunta.setPuntuacion(4);
        pregunta.setEstado(1);
    }

    @Test
    @DisplayName("CP-PREGUNTA-01: Listar preguntas retorna lista completa")
    void testListarPreguntas() {
        when(preguntaEvaluacionRepository.findAll()).thenReturn(Collections.singletonList(pregunta));

        List<PreguntaEvaluacion> resultado = preguntaEvaluacionService.listarPreguntas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("¿Qué es polimorfismo?", resultado.get(0).getEnunciado());
    }

    @Test
    @DisplayName("CP-PREGUNTA-02: Buscar por ID existente retorna la pregunta")
    void testBuscarPorId_Existente() {
        when(preguntaEvaluacionRepository.findById(10)).thenReturn(Optional.of(pregunta));

        PreguntaEvaluacion resultado = preguntaEvaluacionService.buscarPorId(10);

        assertNotNull(resultado);
        assertEquals(10, resultado.getIdPregunta());
    }

    @Test
    @DisplayName("CP-PREGUNTA-03 [Negativo]: Buscar por ID inexistente lanza IllegalArgumentException")
    void testBuscarPorId_NoExiste() {
        when(preguntaEvaluacionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            preguntaEvaluacionService.buscarPorId(99);
        });
    }

    @Test
    @DisplayName("CP-PREGUNTA-04: Guardar pregunta exitosamente asigna estado 1")
    void testGuardarPregunta_Exitoso() {
        when(evaluacionRepository.findById(1)).thenReturn(Optional.of(evaluacion));
        when(preguntaEvaluacionRepository.save(any(PreguntaEvaluacion.class))).thenReturn(pregunta);

        PreguntaEvaluacion resultado = preguntaEvaluacionService.guardarPreguntaEvaluacion(pregunta);

        assertNotNull(resultado);
        assertEquals(1, resultado.getEstado());
        verify(preguntaEvaluacionRepository).save(pregunta);
    }

    @Test
    @DisplayName("CP-PREGUNTA-05 [Negativo]: Guardar pregunta sin tipoPregunta arroja IllegalArgumentException")
    void testGuardarPregunta_SinTipo() {
        pregunta.setTipoPregunta(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            preguntaEvaluacionService.guardarPreguntaEvaluacion(pregunta);
        });
        assertEquals("Tipo pregunta debe estar lleno", ex.getMessage());
    }

    @Test
    @DisplayName("CP-PREGUNTA-06 [Negativo]: Guardar pregunta con puntuación menor o igual a 0 arroja IllegalStateException")
    void testGuardarPregunta_PuntuacionInvalida() {
        pregunta.setPuntuacion(0);

        assertThrows(IllegalStateException.class, () -> {
            preguntaEvaluacionService.guardarPreguntaEvaluacion(pregunta);
        });
    }

    @Test
    @DisplayName("CP-PREGUNTA-07: Actualizar pregunta existente modifica datos")
    void testActualizarPregunta_Exitoso() {
        PreguntaEvaluacion nuevaInfo = new PreguntaEvaluacion();
        nuevaInfo.setEvaluacion(evaluacion);
        nuevaInfo.setTipoPregunta("Verdadero/Falso");
        nuevaInfo.setEnunciado("¿Java compila a bytecode?");
        nuevaInfo.setPuntuacion(5);

        when(preguntaEvaluacionRepository.findById(10)).thenReturn(Optional.of(pregunta));
        when(evaluacionRepository.findById(1)).thenReturn(Optional.of(evaluacion));
        when(preguntaEvaluacionRepository.save(any(PreguntaEvaluacion.class))).thenReturn(pregunta);

        PreguntaEvaluacion resultado = preguntaEvaluacionService.actualizarPreguntaEvaluacion(10, nuevaInfo);

        assertNotNull(resultado);
        verify(preguntaEvaluacionRepository).save(any(PreguntaEvaluacion.class));
    }

    @Test
    @DisplayName("CP-PREGUNTA-08: Eliminar pregunta aplica baja lógica (estado = 0)")
    void testEliminarPregunta_Exitoso() {
        when(preguntaEvaluacionRepository.findById(10)).thenReturn(Optional.of(pregunta));

        preguntaEvaluacionService.eliminarPregEvaluacion(10);

        assertEquals(0, pregunta.getEstado());
        verify(preguntaEvaluacionRepository).save(pregunta);
    }

    @Test
    @DisplayName("CP-PREGUNTA-09 [Negativo]: Eliminar pregunta ya eliminada (estado=0) arroja IllegalStateException")
    void testEliminarPregunta_YaEliminada() {
        pregunta.setEstado(0);
        when(preguntaEvaluacionRepository.findById(10)).thenReturn(Optional.of(pregunta));

        assertThrows(IllegalStateException.class, () -> {
            preguntaEvaluacionService.eliminarPregEvaluacion(10);
        });
    }
}
