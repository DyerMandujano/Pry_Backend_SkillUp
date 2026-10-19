package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.skillup.api.rest.dtos.EvaluacionCursoDTO;
import com.skillup.api.rest.dtos.EvaluacionEstadoDTO;
import com.skillup.api.rest.dtos.EvaluacionResultadoDTO;
import com.skillup.api.rest.dtos.EvaluacionSubmitDTO;
import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Matricula;
import com.skillup.api.rest.model.OpcionPregunta;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.PreguntaEvaluacion;
import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.repository.CursoRepository;
import com.skillup.api.rest.repository.EstudianteRepository;
import com.skillup.api.rest.repository.EvaluacionCursoRepository;
import com.skillup.api.rest.repository.MatriculaRepository;
import com.skillup.api.rest.repository.OpcionPreguntaRepository;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.repository.PreguntaEvaluacionRepository;
import com.skillup.api.rest.repository.RespuestaEstudianteRepository;
import com.skillup.api.rest.repository.SeccionRepository;
import com.skillup.api.rest.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class EvaluacionCursoServiceTest {

    @Mock
    private EvaluacionCursoRepository evaluacionCursoRepository;

    @Mock
    private PreguntaEvaluacionRepository preguntaEvaluacionRepository;

    @Mock
    private OpcionPreguntaRepository opcionPreguntaRepository;

    @Mock
    private RespuestaEstudianteRepository respuestaEstudianteRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SeccionRepository seccionRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private EvaluacionCursoService evaluacionCursoService;

    private Seccion seccionPrueba;
    private Estudiante estudiantePrueba;
    private Persona personaPrueba;
    private Matricula matriculaPrueba;
    private PreguntaEvaluacion pregunta1;
    private OpcionPregunta opcionCorrecta;
    private OpcionPregunta opcionIncorrecta;

    @BeforeEach
    void setUp() {
        seccionPrueba = new Seccion();
        seccionPrueba.setIdSeccion(10);
        seccionPrueba.setIdCurso(1);

        personaPrueba = new Persona();
        personaPrueba.setIdPersona(100);
        personaPrueba.setNombres("Carlos");
        personaPrueba.setApellidos("Mendoza");
        personaPrueba.setEstado(1);

        estudiantePrueba = new Estudiante();
        estudiantePrueba.setIdEstudiante(50);
        estudiantePrueba.setIdPersona(100);
        estudiantePrueba.setEstado(1);

        matriculaPrueba = new Matricula();
        matriculaPrueba.setIdMatricula(1);
        matriculaPrueba.setIdEstudiante(50);
        matriculaPrueba.setIdCurso(1);
        matriculaPrueba.setIntentosExamen(2);
        matriculaPrueba.setEstado(1);

        pregunta1 = new PreguntaEvaluacion();
        pregunta1.setIdPregunta(1);
        pregunta1.setEnunciado("¿Qué anotación define un servicio Spring?");
        pregunta1.setPuntuacion(20);

        opcionCorrecta = new OpcionPregunta();
        opcionCorrecta.setIdOpcPregunta(101);
        opcionCorrecta.setOpcion("@Service");
        opcionCorrecta.setEsCorrecta("SI");
        opcionCorrecta.setPregEvaluacion(pregunta1);

        opcionIncorrecta = new OpcionPregunta();
        opcionIncorrecta.setIdOpcPregunta(102);
        opcionIncorrecta.setOpcion("@Componente");
        opcionIncorrecta.setEsCorrecta("NO");
        opcionIncorrecta.setPregEvaluacion(pregunta1);
    }

    @Test
    @DisplayName("CP-EVAL-01: Listar evaluación por sección retorna preguntas y opciones")
    void testListarEvaluacion() {
        EvaluacionCursoDTO dto = new EvaluacionCursoDTO();
        dto.setIdPregunta(1);
        dto.setEnunciado("Pregunta 1");

        when(evaluacionCursoRepository.listarEvaluacionPorSeccion(10))
            .thenReturn(Collections.singletonList(dto));

        List<EvaluacionCursoDTO> resultado = evaluacionCursoService.listarEvaluacion(10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(evaluacionCursoRepository).listarEvaluacionPorSeccion(10);
    }

    @Test
    @DisplayName("CP-EVAL-02: Obtener estado de evaluación retorna intentos restantes")
    void testObtenerEstadoEvaluacion_ConIntentosDisponibles() {
        when(seccionRepository.findById(10)).thenReturn(Optional.of(seccionPrueba));
        when(cursoRepository.findById(1)).thenReturn(Optional.of(new Curso()));
        when(preguntaEvaluacionRepository.findBySeccion(10)).thenReturn(Collections.singletonList(pregunta1));
        when(estudianteRepository.findById(50)).thenReturn(Optional.of(estudiantePrueba));
        when(personaRepository.findById(100)).thenReturn(Optional.of(personaPrueba));
        when(matriculaRepository.findFirstByIdEstudianteAndIdCurso(50, 1)).thenReturn(Optional.of(matriculaPrueba));
        when(respuestaEstudianteRepository.findRespuestasByEstudianteYSeccion(50, 10)).thenReturn(Collections.emptyList());

        EvaluacionEstadoDTO estado = evaluacionCursoService.obtenerEstadoEvaluacion(10, 50);

        assertNotNull(estado);
        assertEquals(2, estado.getIntentosRestantes());
        assertFalse(estado.isCuentaDeshabilitada());
        assertFalse(estado.isYaAprobado());
    }

    @Test
    @DisplayName("CP-EVAL-03: Evaluar respuestas correctas al 100% aprueba evaluación")
    void testEvaluarRespuestas_Aprobado100Porciento() {
        when(seccionRepository.findById(10)).thenReturn(Optional.of(seccionPrueba));
        when(estudianteRepository.findById(50)).thenReturn(Optional.of(estudiantePrueba));
        when(personaRepository.findById(100)).thenReturn(Optional.of(personaPrueba));
        when(matriculaRepository.findFirstByIdEstudianteAndIdCurso(50, 1)).thenReturn(Optional.of(matriculaPrueba));
        when(preguntaEvaluacionRepository.findBySeccion(10)).thenReturn(Collections.singletonList(pregunta1));
        when(respuestaEstudianteRepository.findRespuestasByEstudianteYSeccion(50, 10)).thenReturn(Collections.emptyList());
        when(opcionPreguntaRepository.findById(101)).thenReturn(Optional.of(opcionCorrecta));
        when(opcionPreguntaRepository.findCorrectaByPregunta(1)).thenReturn(Optional.of(opcionCorrecta));

        EvaluacionSubmitDTO submitDTO = new EvaluacionSubmitDTO();
        submitDTO.setIdEstudiante(50);
        EvaluacionSubmitDTO.RespuestaItemDTO item = new EvaluacionSubmitDTO.RespuestaItemDTO();
        item.setIdPregunta(1);
        item.setIdOpPregunta(101); // Opción correcta
        submitDTO.setRespuestas(Collections.singletonList(item));

        EvaluacionResultadoDTO resultado = evaluacionCursoService.evaluarYGuardarRespuestas(10, submitDTO);

        assertNotNull(resultado);
        assertTrue(resultado.isAprobado());
        assertEquals(1, resultado.getCorrectas());
        assertEquals(0, resultado.getIncorrectas());
        assertEquals("1/1", resultado.getContador());
        assertFalse(resultado.isCuentaDeshabilitada());
    }

    @Test
    @DisplayName("CP-EVAL-04: Estudiante no matriculado lanza excepción controlada")
    void testEvaluarRespuestas_NoMatriculado_LanzaExcepcion() {
        when(seccionRepository.findById(10)).thenReturn(Optional.of(seccionPrueba));
        when(estudianteRepository.findById(50)).thenReturn(Optional.of(estudiantePrueba));
        when(personaRepository.findById(100)).thenReturn(Optional.of(personaPrueba));
        when(matriculaRepository.findFirstByIdEstudianteAndIdCurso(50, 1)).thenReturn(Optional.empty());

        EvaluacionSubmitDTO submitDTO = new EvaluacionSubmitDTO();
        submitDTO.setIdEstudiante(50);

        assertThrows(RuntimeException.class, () -> {
            evaluacionCursoService.evaluarYGuardarRespuestas(10, submitDTO);
        });
    }

    @Test
    @DisplayName("CP-EVAL-05: Falla 2 intentos agota límite y deshabilita la cuenta del estudiante")
    void testEvaluarRespuestas_AgotarIntentos_DeshabilitaCuenta() {
        // Estudiante con 1 solo intento restante
        matriculaPrueba.setIntentosExamen(1);

        when(seccionRepository.findById(10)).thenReturn(Optional.of(seccionPrueba));
        when(estudianteRepository.findById(50)).thenReturn(Optional.of(estudiantePrueba));
        when(personaRepository.findById(100)).thenReturn(Optional.of(personaPrueba));
        when(matriculaRepository.findFirstByIdEstudianteAndIdCurso(50, 1)).thenReturn(Optional.of(matriculaPrueba));
        when(preguntaEvaluacionRepository.findBySeccion(10)).thenReturn(Collections.singletonList(pregunta1));
        when(respuestaEstudianteRepository.findRespuestasByEstudianteYSeccion(50, 10)).thenReturn(Collections.emptyList());
        when(opcionPreguntaRepository.findById(102)).thenReturn(Optional.of(opcionIncorrecta));
        when(opcionPreguntaRepository.findCorrectaByPregunta(1)).thenReturn(Optional.of(opcionCorrecta));
        when(usuarioRepository.findByIdPersona(100)).thenReturn(Optional.of(new Usuario()));

        EvaluacionSubmitDTO submitDTO = new EvaluacionSubmitDTO();
        submitDTO.setIdEstudiante(50);
        EvaluacionSubmitDTO.RespuestaItemDTO item = new EvaluacionSubmitDTO.RespuestaItemDTO();
        item.setIdPregunta(1);
        item.setIdOpPregunta(102); // Incorrecta
        submitDTO.setRespuestas(Collections.singletonList(item));

        EvaluacionResultadoDTO resultado = evaluacionCursoService.evaluarYGuardarRespuestas(10, submitDTO);

        assertNotNull(resultado);
        assertFalse(resultado.isAprobado());
        assertTrue(resultado.isCuentaDeshabilitada());
        assertEquals(0, resultado.getIntentosRestantes());
        assertEquals(0, personaPrueba.getEstado());
        assertEquals(0, estudiantePrueba.getEstado());
    }
}
