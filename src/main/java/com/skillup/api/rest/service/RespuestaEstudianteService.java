package com.skillup.api.rest.service;

import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.OpcionPregunta;
import com.skillup.api.rest.model.RespuestaEstudiante;
import com.skillup.api.rest.repository.EstudianteRepository;
import com.skillup.api.rest.repository.OpcionPreguntaRepository;
import com.skillup.api.rest.repository.RespuestaEstudianteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class RespuestaEstudianteService {

    @Autowired
    private RespuestaEstudianteRepository respuestaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private OpcionPreguntaRepository opcionPreguntaRepository;

    public List<RespuestaEstudiante> listarRespuestas() {
        return respuestaRepository.findAll();
    }

    public RespuestaEstudiante buscarPorId(int id) {
        return respuestaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Respuesta no encontrada"));
    }

    public RespuestaEstudiante guardarRespuesta(RespuestaEstudiante respuesta) {
        if (respuesta.getEstudiante() == null) {
            throw new IllegalArgumentException("Debe ingresar un estudiante valido");
        }
        if (respuesta.getOpcionPregunta() == null) {
            throw new IllegalArgumentException("Debe ingresar una opción de pregunta valida");
        }
        
        if(respuesta.getTextoRespuesta()==null) {
        	throw new IllegalArgumentException("El texto de la respuesta no va vacio");
        }
        if (respuesta.getPuntaje() < 0) {
            throw new IllegalStateException("El puntaje no puede ser negativo");
        }
        if (respuesta.getFecRespuesta()==null) {
        	throw new IllegalStateException("La fecha no puede ser vacia");
        }

        int idEst = respuesta.getEstudiante().getIdEstudiante();
        Estudiante est = estudianteRepository.findById(idEst)
                .orElseThrow(() -> new IllegalArgumentException("Id de estudiante no existe"));

        int idOp = respuesta.getOpcionPregunta().getIdOpcPregunta();
        OpcionPregunta op = opcionPreguntaRepository.findById(idOp)
                .orElseThrow(() -> new IllegalArgumentException("Id de opcion no existe"));

        respuesta.setEstudiante(est);
        respuesta.setOpcionPregunta(op);
        respuesta.setEstado(1);

        return respuestaRepository.save(respuesta);
    }

    public RespuestaEstudiante actualizarRespuesta(int id, RespuestaEstudiante nueva) {
        RespuestaEstudiante existente = respuestaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Respuesta no encontrada"));

        if (nueva.getTextoRespuesta() != null)
            existente.setTextoRespuesta(nueva.getTextoRespuesta());
        
        if (nueva.getEstudiante() == null) {
            throw new IllegalArgumentException("Debe ingresar un estudiante valido");
        }
        if (nueva.getOpcionPregunta() == null) {
            throw new IllegalArgumentException("Debe ingresar una opción de pregunta valida");
        }
        
        if(nueva.getTextoRespuesta()==null) {
        	throw new IllegalArgumentException("El texto de la respuesta no va vacio");
        }
        if (nueva.getPuntaje() < 0) {
            throw new IllegalStateException("El puntaje no puede ser negativo");
        }
        if (nueva.getFecRespuesta()==null) {
        	throw new IllegalStateException("La fecha no puede ser vacia");
        }

        int idEst = nueva.getEstudiante().getIdEstudiante();
        Estudiante est = estudianteRepository.findById(idEst)
                .orElseThrow(() -> new IllegalArgumentException("Id de estudiante no existe"));

        int idOp = nueva.getOpcionPregunta().getIdOpcPregunta();
        OpcionPregunta op = opcionPreguntaRepository.findById(idOp)
                .orElseThrow(() -> new IllegalArgumentException("Id de opcion no existe"));
        
        existente.setEstudiante(est);
        existente.setOpcionPregunta(op);
        existente.setTextoRespuesta(nueva.getTextoRespuesta());
        existente.setPuntaje(nueva.getPuntaje());
        existente.setFecRespuesta(nueva.getFecRespuesta());
        existente.setEstado(nueva.getEstado());

        return respuestaRepository.save(existente);
    }

    public void eliminarRespuesta(int id) {
        RespuestaEstudiante respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id no encontrado"));
        if (respuesta.getEstado() == 0) {
            throw new IllegalStateException("La respuesta ya fue eliminada anteriormente");
        }
        respuesta.setEstado(0);
        respuestaRepository.save(respuesta);
    }
}
