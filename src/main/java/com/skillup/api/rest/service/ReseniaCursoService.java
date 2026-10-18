package com.skillup.api.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillup.api.rest.dtos.RegistrarReseniaRequestDTO;
import com.skillup.api.rest.dtos.ReseniaCursoDTO;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.ReseniaCurso;
import com.skillup.api.rest.repository.EstudianteRepository;
import com.skillup.api.rest.repository.MatriculaRepository;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.repository.ReseniaCursoRepository;

@Service
public class ReseniaCursoService {

    @Autowired
    private ReseniaCursoRepository reseniaCursoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    /**
     * HU20: Listado de reseñas públicas de un curso ordenadas por fecha reciente
     */
    public List<ReseniaCursoDTO> listarReseniasPorCurso(Integer idCurso) {
        if (idCurso == null || idCurso <= 0) {
            throw new RuntimeException("El identificador del curso no es válido");
        }

        List<ReseniaCurso> resenias = reseniaCursoRepository.findByIdCursoAndEstadoOrderByFechaReseniaDesc(idCurso, 1);
        List<ReseniaCursoDTO> resultado = new ArrayList<>();

        for (ReseniaCurso r : resenias) {
            ReseniaCursoDTO dto = new ReseniaCursoDTO();
            dto.setIdReseniaCurso(r.getIdReseniaCurso());
            dto.setIdCurso(r.getIdCurso());
            dto.setIdEstudiante(r.getIdEstudiante());
            dto.setCalificacion(r.getCalificacion());
            dto.setComentario(r.getComentario());
            dto.setFechaResenia(r.getFechaResenia());
            dto.setEstado(r.getEstado());

            // Obtener nombre del estudiante
            String nombreEstudiante = "Estudiante #" + r.getIdEstudiante();
            try {
                Estudiante est = estudianteRepository.findById(r.getIdEstudiante()).orElse(null);
                if (est != null) {
                    Persona p = personaRepository.findById(est.getIdPersona()).orElse(null);
                    if (p != null) {
                        nombreEstudiante = p.getNombres() + " " + p.getApellidos();
                    }
                }
            } catch (Exception ignored) {
            }
            dto.setNombreEstudiante(nombreEstudiante);

            resultado.add(dto);
        }

        return resultado;
    }

    /**
     * HU20 - RF-28: Registrar reseña y valoración de un curso
     * Escenario 1: Estudiante matriculado califica (1-5 estrellas) y comenta.
     * Escenario 2: Comentario sin estrellas -> Bloquea y exige puntuación.
     * Escenario 3: Usuario sin matrícula -> Bloquea indicando requisito de inscripción.
     */
    @Transactional
    public ReseniaCursoDTO registrarResenia(Integer idCurso, RegistrarReseniaRequestDTO request) {
        if (idCurso == null || idCurso <= 0) {
            throw new RuntimeException("El identificador del curso no es válido.");
        }
        if (request == null) {
            throw new RuntimeException("Los datos de la reseña son obligatorios.");
        }
        if (request.getIdEstudiante() == null || request.getIdEstudiante() <= 0) {
            throw new RuntimeException("El identificador del estudiante es obligatorio.");
        }

        // Escenario 2: Validación obligatoria de puntuación de 1 a 5 estrellas
        if (request.getCalificacion() == null || request.getCalificacion() < 1 || request.getCalificacion() > 5) {
            throw new RuntimeException("La calificación es obligatoria y debe ser entre 1 y 5 estrellas.");
        }

        if (request.getComentario() == null || request.getComentario().trim().isEmpty()) {
            throw new RuntimeException("El comentario de la reseña es obligatorio.");
        }

        // Escenario 3: Restricción de matrícula activa
        boolean matriculado = matriculaRepository.existsByIdEstudianteAndIdCurso(request.getIdEstudiante(), idCurso);
        if (!matriculado) {
            throw new RuntimeException("Solo los estudiantes matriculados en este curso pueden registrar una reseña.");
        }

        // Guardar reseña
        ReseniaCurso resenia = new ReseniaCurso();
        resenia.setIdCurso(idCurso);
        resenia.setIdEstudiante(request.getIdEstudiante());
        resenia.setCalificacion(request.getCalificacion());
        resenia.setComentario(request.getComentario().trim());
        resenia.setFechaResenia(new Date());
        resenia.setEstado(1); // 1 = Publicada / Visible

        ReseniaCurso guardada = reseniaCursoRepository.save(resenia);

        // Armar DTO de retorno
        ReseniaCursoDTO dto = new ReseniaCursoDTO();
        dto.setIdReseniaCurso(guardada.getIdReseniaCurso());
        dto.setIdCurso(guardada.getIdCurso());
        dto.setIdEstudiante(guardada.getIdEstudiante());
        dto.setCalificacion(guardada.getCalificacion());
        dto.setComentario(guardada.getComentario());
        dto.setFechaResenia(guardada.getFechaResenia());
        dto.setEstado(guardada.getEstado());

        String nombreEstudiante = "Estudiante #" + guardada.getIdEstudiante();
        try {
            Estudiante est = estudianteRepository.findById(guardada.getIdEstudiante()).orElse(null);
            if (est != null) {
                Persona p = personaRepository.findById(est.getIdPersona()).orElse(null);
                if (p != null) {
                    nombreEstudiante = p.getNombres() + " " + p.getApellidos();
                }
            }
        } catch (Exception ignored) {
        }
        dto.setNombreEstudiante(nombreEstudiante);

        return dto;
    }
}
