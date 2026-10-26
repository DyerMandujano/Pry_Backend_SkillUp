package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.OpcionPregunta;
import com.skillup.api.rest.model.RespuestaEstudiante;
import com.skillup.api.rest.repository.EstudianteRepository;
import com.skillup.api.rest.repository.OpcionPreguntaRepository;
import com.skillup.api.rest.repository.RespuestaEstudianteRepository;

@ExtendWith(MockitoExtension.class)
public class RespuestaEstudianteServiceTest {

    @Mock
    private RespuestaEstudianteRepository respuestaRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private OpcionPreguntaRepository opcionPreguntaRepository;

    @InjectMocks
    private RespuestaEstudianteService respuestaEstudianteService;

    private RespuestaEstudiante respuesta;
    private Estudiante estudiante;
    private OpcionPregunta opcion;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante();
        estudiante.setIdEstudiante(1);

        opcion = new OpcionPregunta();
        opcion.setIdOpcPregunta(5);

        respuesta = new RespuestaEstudiante();
        respuesta.setIdRespEstudiante(50);
        respuesta.setEstudiante(estudiante);
        respuesta.setOpcionPregunta(opcion);
        respuesta.setTextoRespuesta("Opción seleccionada: A");
        respuesta.setPuntaje(5);
        respuesta.setFecRespuesta(new Date());
        respuesta.setEstado(1);
    }

    @Test
    @DisplayName("CP-RESP-01: Listar respuestas retorna colección completa")
    void testListarRespuestas() {
        when(respuestaRepository.findAll()).thenReturn(Collections.singletonList(respuesta));

        List<RespuestaEstudiante> resultado = respuestaEstudianteService.listarRespuestas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Opción seleccionada: A", resultado.get(0).getTextoRespuesta());
    }

    @Test
    @DisplayName("CP-RESP-02: Buscar por ID existente retorna la respuesta")
    void testBuscarPorId_Existente() {
        when(respuestaRepository.findById(50)).thenReturn(Optional.of(respuesta));

        RespuestaEstudiante resultado = respuestaEstudianteService.buscarPorId(50);

        assertNotNull(resultado);
        assertEquals(50, resultado.getIdRespEstudiante());
    }

    @Test
    @DisplayName("CP-RESP-03 [Negativo]: Buscar ID inexistente lanza IllegalArgumentException")
    void testBuscarPorId_Inexistente() {
        when(respuestaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            respuestaEstudianteService.buscarPorId(999);
        });
    }

    @Test
    @DisplayName("CP-RESP-04: Guardar respuesta exitosa valida relaciones y persiste entidad")
    void testGuardarRespuesta_Exitoso() {
        when(estudianteRepository.findById(1)).thenReturn(Optional.of(estudiante));
        when(opcionPreguntaRepository.findById(5)).thenReturn(Optional.of(opcion));
        when(respuestaRepository.save(any(RespuestaEstudiante.class))).thenReturn(respuesta);

        RespuestaEstudiante resultado = respuestaEstudianteService.guardarRespuesta(respuesta);

        assertNotNull(resultado);
        assertEquals(1, resultado.getEstado());
        verify(respuestaRepository).save(respuesta);
    }

    @Test
    @DisplayName("CP-RESP-05 [Negativo]: Guardar respuesta con puntaje negativo lanza IllegalStateException")
    void testGuardarRespuesta_PuntajeNegativo() {
        respuesta.setPuntaje(-1);

        assertThrows(IllegalStateException.class, () -> {
            respuestaEstudianteService.guardarRespuesta(respuesta);
        });
    }

    @Test
    @DisplayName("CP-RESP-06 [Negativo]: Guardar respuesta sin texto lanza IllegalArgumentException")
    void testGuardarRespuesta_SinTexto() {
        respuesta.setTextoRespuesta(null);

        assertThrows(IllegalArgumentException.class, () -> {
            respuestaEstudianteService.guardarRespuesta(respuesta);
        });
    }

    @Test
    @DisplayName("CP-RESP-07: Eliminar respuesta aplica baja lógica")
    void testEliminarRespuesta_Exitoso() {
        when(respuestaRepository.findById(50)).thenReturn(Optional.of(respuesta));

        respuestaEstudianteService.eliminarRespuesta(50);

        assertEquals(0, respuesta.getEstado());
        verify(respuestaRepository).save(respuesta);
    }

    @Test
    @DisplayName("CP-RESP-08 [Negativo]: Eliminar respuesta ya eliminada lanza IllegalStateException")
    void testEliminarRespuesta_YaEliminada() {
        respuesta.setEstado(0);
        when(respuestaRepository.findById(50)).thenReturn(Optional.of(respuesta));

        assertThrows(IllegalStateException.class, () -> {
            respuestaEstudianteService.eliminarRespuesta(50);
        });
    }
}
