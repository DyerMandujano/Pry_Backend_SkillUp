package com.skillup.api.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.skillup.api.rest.model.RespuestaEstudiante;
import com.skillup.api.rest.model.Seccion;
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

@Service
public class EvaluacionCursoService {

    @Autowired
    private EvaluacionCursoRepository evaluacionCursoRepository;

    @Autowired
    private PreguntaEvaluacionRepository preguntaEvaluacionRepository;

    @Autowired
    private OpcionPreguntaRepository opcionPreguntaRepository;

    @Autowired
    private RespuestaEstudianteRepository respuestaEstudianteRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SeccionRepository seccionRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<EvaluacionCursoDTO> listarEvaluacion(int idSeccion) {
        return evaluacionCursoRepository.listarEvaluacionPorSeccion(idSeccion);
    }

    public EvaluacionEstadoDTO obtenerEstadoEvaluacion(int idSeccion, int idEstudiante) {
        EvaluacionEstadoDTO estadoDTO = new EvaluacionEstadoDTO();
        estadoDTO.setIdSeccion(idSeccion);

        Seccion seccion = seccionRepository.findById(idSeccion).orElse(null);
        int idCurso = 0;
        if (seccion != null && seccion.getIdCurso() != null) {
            idCurso = seccion.getIdCurso();
        }
        estadoDTO.setIdCurso(idCurso);

        if (idCurso > 0) {
            Curso curso = cursoRepository.findById(idCurso).orElse(null);
            if (curso != null) {
                estadoDTO.setNombreCurso(curso.getNombreCurso());
            }
        }

        List<PreguntaEvaluacion> preguntas = preguntaEvaluacionRepository.findBySeccion(idSeccion);
        estadoDTO.setTotalPreguntas(preguntas.size());

        Estudiante estudiante = estudianteRepository.findById(idEstudiante).orElse(null);
        boolean cuentaDeshabilitada = false;
        if (estudiante != null) {
            if (estudiante.getEstado() == 0) {
                cuentaDeshabilitada = true;
            } else {
                Persona persona = personaRepository.findById(estudiante.getIdPersona()).orElse(null);
                if (persona != null && persona.getEstado() == 0) {
                    cuentaDeshabilitada = true;
                }
            }
        }

        int intentosRestantes = 2;
        if (idCurso > 0 && idEstudiante > 0) {
            Optional<Matricula> matOpt = matriculaRepository.findFirstByIdEstudianteAndIdCurso(idEstudiante, idCurso);
            if (matOpt.isPresent()) {
                Matricula m = matOpt.get();
                if (m.getIntentosExamen() != null) {
                    intentosRestantes = m.getIntentosExamen();
                }
            }
        }
        estadoDTO.setIntentosRestantes(intentosRestantes);

        if (intentosRestantes <= 0) {
            cuentaDeshabilitada = true;
        }
        estadoDTO.setCuentaDeshabilitada(cuentaDeshabilitada);

        // Verificar si ya aprobó con todas las respuestas correctas
        boolean yaAprobado = false;
        if (preguntas.size() > 0 && idEstudiante > 0) {
            List<RespuestaEstudiante> respuestas = respuestaEstudianteRepository.findRespuestasByEstudianteYSeccion(idEstudiante, idSeccion);
            long correctas = respuestas.stream()
                .filter(r -> r.getOpcionPregunta() != null && "SI".equalsIgnoreCase(r.getOpcionPregunta().getEsCorrecta()))
                .count();
            if (correctas >= preguntas.size()) {
                yaAprobado = true;
            }
        }
        estadoDTO.setYaAprobado(yaAprobado);

        if (cuentaDeshabilitada) {
            estadoDTO.setMensaje("Su cuenta ha sido deshabilitada por límite de intentos al rendir la evaluación final.");
        } else if (yaAprobado) {
            estadoDTO.setMensaje("Ya has aprobado esta evaluación con el 100% de respuestas correctas.");
        } else {
            estadoDTO.setMensaje("Tienes " + intentosRestantes + " intento(s) disponible(s).");
        }

        return estadoDTO;
    }

    @Transactional
    public EvaluacionResultadoDTO evaluarYGuardarRespuestas(int idSeccion, EvaluacionSubmitDTO dto) {
        Seccion seccion = seccionRepository.findById(idSeccion)
            .orElseThrow(() -> new RuntimeException("Sección no encontrada: " + idSeccion));
        int idCurso = seccion.getIdCurso() != null ? seccion.getIdCurso() : 0;

        Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + dto.getIdEstudiante()));
        Persona persona = personaRepository.findById(estudiante.getIdPersona())
            .orElseThrow(() -> new RuntimeException("Persona no encontrada para el estudiante"));

        // Verificar si ya está deshabilitado
        if (persona.getEstado() == 0 || estudiante.getEstado() == 0) {
            EvaluacionResultadoDTO err = new EvaluacionResultadoDTO();
            err.setCuentaDeshabilitada(true);
            err.setIntentosRestantes(0);
            err.setMensaje("Su cuenta ha sido deshabilitada por límite de intentos al rendir la evaluación final.");
            return err;
        }

        Matricula matricula = matriculaRepository.findFirstByIdEstudianteAndIdCurso(dto.getIdEstudiante(), idCurso)
            .orElseThrow(() -> new RuntimeException("El estudiante no está matriculado en este curso."));

        int intentosActuales = matricula.getIntentosExamen() != null ? matricula.getIntentosExamen() : 2;
        if (intentosActuales <= 0) {
            EvaluacionResultadoDTO err = new EvaluacionResultadoDTO();
            err.setCuentaDeshabilitada(true);
            err.setIntentosRestantes(0);
            err.setMensaje("Su cuenta ha sido deshabilitada por límite de intentos al rendir la evaluación final.");
            return err;
        }

        List<PreguntaEvaluacion> preguntas = preguntaEvaluacionRepository.findBySeccion(idSeccion);
        int totalPreguntas = preguntas.size();

        // Desactivar respuestas previas activas de este estudiante en esta sección
        List<RespuestaEstudiante> previas = respuestaEstudianteRepository.findRespuestasByEstudianteYSeccion(dto.getIdEstudiante(), idSeccion);
        for (RespuestaEstudiante p : previas) {
            p.setEstado(0);
        }
        if (!previas.isEmpty()) {
            respuestaEstudianteRepository.saveAll(previas);
        }

        Map<Integer, Integer> mapaRespuestas = new HashMap<>();
        if (dto.getRespuestas() != null) {
            for (EvaluacionSubmitDTO.RespuestaItemDTO item : dto.getRespuestas()) {
                mapaRespuestas.put(item.getIdPregunta(), item.getIdOpPregunta());
            }
        }

        int correctas = 0;
        int incorrectas = 0;
        int puntajeTotal = 0;
        int puntajeMaximo = 0;
        List<EvaluacionResultadoDTO.DetallePreguntaDTO> detalles = new ArrayList<>();
        Date fechaHoy = new Date();

        for (PreguntaEvaluacion preg : preguntas) {
            puntajeMaximo += preg.getPuntuacion();
            Integer idOpSeleccionada = mapaRespuestas.get(preg.getIdPregunta());

            OpcionPregunta opcionSeleccionada = null;
            if (idOpSeleccionada != null) {
                opcionSeleccionada = opcionPreguntaRepository.findById(idOpSeleccionada).orElse(null);
            }

            Optional<OpcionPregunta> opCorrectaOpt = opcionPreguntaRepository.findCorrectaByPregunta(preg.getIdPregunta());
            Integer idOpCorrecta = opCorrectaOpt.map(OpcionPregunta::getIdOpcPregunta).orElse(null);

            boolean esCorrecta = false;
            if (opcionSeleccionada != null && "SI".equalsIgnoreCase(opcionSeleccionada.getEsCorrecta())) {
                esCorrecta = true;
                correctas++;
                puntajeTotal += preg.getPuntuacion();
            } else {
                incorrectas++;
            }

            if (opcionSeleccionada != null) {
                RespuestaEstudiante resp = new RespuestaEstudiante();
                resp.setEstudiante(estudiante);
                resp.setOpcionPregunta(opcionSeleccionada);
                resp.setTextoRespuesta(opcionSeleccionada.getOpcion());
                resp.setPuntaje(esCorrecta ? preg.getPuntuacion() : 0);
                resp.setFecRespuesta(fechaHoy);
                resp.setEstado(1);
                respuestaEstudianteRepository.save(resp);
            }

            EvaluacionResultadoDTO.DetallePreguntaDTO det = new EvaluacionResultadoDTO.DetallePreguntaDTO();
            det.setIdPregunta(preg.getIdPregunta());
            det.setIdOpSeleccionada(idOpSeleccionada != null ? idOpSeleccionada : 0);
            det.setEsCorrecta(esCorrecta);
            det.setIdOpCorrecta(idOpCorrecta);
            detalles.add(det);
        }

        boolean aprobado = (totalPreguntas > 0 && correctas == totalPreguntas);
        EvaluacionResultadoDTO resultado = new EvaluacionResultadoDTO();
        resultado.setTotalPreguntas(totalPreguntas);
        resultado.setCorrectas(correctas);
        resultado.setIncorrectas(incorrectas);
        resultado.setContador(correctas + "/" + totalPreguntas);
        resultado.setPuntajeTotal(puntajeTotal);
        resultado.setPuntajeMaximo(puntajeMaximo);
        resultado.setAprobado(aprobado);
        resultado.setDetalles(detalles);

        if (aprobado) {
            resultado.setIntentosRestantes(intentosActuales);
            resultado.setCuentaDeshabilitada(false);
            resultado.setMensaje("¡Felicitaciones! Has respondido todas las preguntas correctamente (" + correctas + "/" + totalPreguntas + "). Ya puedes obtener tu certificado.");
        } else {
            int nuevosIntentos = intentosActuales - 1;
            if (nuevosIntentos < 0) nuevosIntentos = 0;
            matricula.setIntentosExamen(nuevosIntentos);
            matriculaRepository.save(matricula);
            resultado.setIntentosRestantes(nuevosIntentos);

            if (nuevosIntentos <= 0) {
                // Deshabilitar cuenta del estudiante por agotar los 2 intentos
                estudiante.setEstado(0);
                estudianteRepository.save(estudiante);

                persona.setEstado(0);
                personaRepository.save(persona);

                usuarioRepository.findByIdPersona(estudiante.getIdPersona()).ifPresent(u -> {
                    u.setIntentosFallidos(3);
                    u.setFecBloqueoHasta(new Date(System.currentTimeMillis() + 100L * 365 * 24 * 3600 * 1000));
                    usuarioRepository.save(u);
                });

                resultado.setCuentaDeshabilitada(true);
                resultado.setMensaje("Su cuenta ha sido deshabilitada por límite de intentos al rendir la evaluación final.");
            } else {
                resultado.setCuentaDeshabilitada(false);
                resultado.setMensaje("Has contestado " + correctas + " correctas y " + incorrectas + " incorrectas (" + resultado.getContador() + "). Te queda " + nuevosIntentos + " intento(s) para rendir la evaluación.");
            }
        }

        return resultado;
    }

    public boolean haAprobadoEvaluacion(int idEstudiante, int idCurso) {
        List<PreguntaEvaluacion> preguntas = preguntaEvaluacionRepository.findByCurso(idCurso);
        if (preguntas.isEmpty()) {
            return true;
        }

        List<RespuestaEstudiante> respuestas = respuestaEstudianteRepository.findRespuestasByEstudianteYCurso(idEstudiante, idCurso);
        Set<Integer> preguntasRespondidasCorrectamente = respuestas.stream()
            .filter(r -> r.getOpcionPregunta() != null && "SI".equalsIgnoreCase(r.getOpcionPregunta().getEsCorrecta()))
            .map(r -> r.getOpcionPregunta().getPregEvaluacion().getIdPregunta())
            .collect(Collectors.toSet());

        for (PreguntaEvaluacion p : preguntas) {
            if (!preguntasRespondidasCorrectamente.contains(p.getIdPregunta())) {
                return false;
            }
        }
        return true;
    }
}
