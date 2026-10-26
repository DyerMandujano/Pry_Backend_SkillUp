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

import com.skillup.api.rest.model.OpcionPregunta;
import com.skillup.api.rest.model.PreguntaEvaluacion;
import com.skillup.api.rest.repository.OpcionPreguntaRepository;
import com.skillup.api.rest.repository.PreguntaEvaluacionRepository;

@ExtendWith(MockitoExtension.class)
public class OpcionPreguntaServiceTest {

    @Mock
    private OpcionPreguntaRepository opcionPreguntaRepository;

    @Mock
    private PreguntaEvaluacionRepository preguntaEvaluacionRepository;

    @InjectMocks
    private OpcionPreguntaService opcionPreguntaService;

    private OpcionPregunta opcion;
    private PreguntaEvaluacion pregunta;

    @BeforeEach
    void setUp() {
        pregunta = new PreguntaEvaluacion();
        pregunta.setIdPregunta(1);

        opcion = new OpcionPregunta();
        opcion.setIdOpcPregunta(100);
        opcion.setPregEvaluacion(pregunta);
        opcion.setOpcion("Compilador Just-In-Time");
        opcion.setEsCorrecta("1");
        opcion.setEstado(1);
    }

    @Test
    @DisplayName("CP-OPCION-01: Listar opciones retorna lista de la BD")
    void testListarOpciones() {
        when(opcionPreguntaRepository.findAll()).thenReturn(Collections.singletonList(opcion));

        List<OpcionPregunta> resultado = opcionPreguntaService.listarOpcionPreguntas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Compilador Just-In-Time", resultado.get(0).getOpcion());
    }

    @Test
    @DisplayName("CP-OPCION-02: Buscar por ID existente retorna la opción")
    void testBuscarPorId_Existente() {
        when(opcionPreguntaRepository.findById(100)).thenReturn(Optional.of(opcion));

        OpcionPregunta resultado = opcionPreguntaService.buscarPorId(100);

        assertNotNull(resultado);
        assertEquals(100, resultado.getIdOpcPregunta());
    }

    @Test
    @DisplayName("CP-OPCION-03 [Negativo]: Buscar opción inexistente lanza IllegalArgumentException")
    void testBuscarPorId_NoExiste() {
        when(opcionPreguntaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            opcionPreguntaService.buscarPorId(999);
        });
    }

    @Test
    @DisplayName("CP-OPCION-04: Guardar opción exitosamente vincula con pregunta y asigna estado 1")
    void testGuardarOpcion_Exitoso() {
        when(preguntaEvaluacionRepository.findById(1)).thenReturn(Optional.of(pregunta));
        when(opcionPreguntaRepository.save(any(OpcionPregunta.class))).thenReturn(opcion);

        OpcionPregunta resultado = opcionPreguntaService.guardarOpcionPregunta(opcion);

        assertNotNull(resultado);
        assertEquals(1, resultado.getEstado());
        verify(opcionPreguntaRepository).save(opcion);
    }

    @Test
    @DisplayName("CP-OPCION-05 [Negativo]: Guardar opción sin texto lanza IllegalArgumentException")
    void testGuardarOpcion_SinTexto() {
        opcion.setOpcion(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            opcionPreguntaService.guardarOpcionPregunta(opcion);
        });
        assertEquals("Campo opcion debe estar lleno", ex.getMessage());
    }

    @Test
    @DisplayName("CP-OPCION-06 [Negativo]: Guardar opción con texto mayor a 100 caracteres lanza IllegalStateException")
    void testGuardarOpcion_TextoLargo() {
        opcion.setOpcion("a".repeat(101));

        assertThrows(IllegalStateException.class, () -> {
            opcionPreguntaService.guardarOpcionPregunta(opcion);
        });
    }

    @Test
    @DisplayName("CP-OPCION-07: Actualizar opción modifica campos y guarda")
    void testActualizarOpcion_Exitoso() {
        OpcionPregunta nueva = new OpcionPregunta();
        nueva.setOpcion("Nueva opción válida");
        nueva.setEsCorrecta("0");
        nueva.setPregEvaluacion(pregunta);
        nueva.setEstado(1);

        when(opcionPreguntaRepository.findById(100)).thenReturn(Optional.of(opcion));
        when(preguntaEvaluacionRepository.findById(1)).thenReturn(Optional.of(pregunta));
        when(opcionPreguntaRepository.save(any(OpcionPregunta.class))).thenReturn(opcion);

        OpcionPregunta resultado = opcionPreguntaService.actualizarOpcionPregunta(100, nueva);

        assertNotNull(resultado);
        verify(opcionPreguntaRepository).save(any(OpcionPregunta.class));
    }

    @Test
    @DisplayName("CP-OPCION-08: Eliminar opción aplica baja lógica (estado = 0)")
    void testEliminarOpcion_Exitoso() {
        when(opcionPreguntaRepository.findById(100)).thenReturn(Optional.of(opcion));

        opcionPreguntaService.eliminarOpcPregunta(100);

        assertEquals(0, opcion.getEstado());
        verify(opcionPreguntaRepository).save(opcion);
    }

    @Test
    @DisplayName("CP-OPCION-09 [Negativo]: Eliminar opción ya eliminada lanza IllegalStateException")
    void testEliminarOpcion_YaEliminada() {
        opcion.setEstado(0);
        when(opcionPreguntaRepository.findById(100)).thenReturn(Optional.of(opcion));

        assertThrows(IllegalStateException.class, () -> {
            opcionPreguntaService.eliminarOpcPregunta(100);
        });
    }
}
