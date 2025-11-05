package com.skillup.api.rest.service;

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.model.PreguntaEvaluacion;
import com.skillup.api.rest.repository.EvaluacionRepository;
import com.skillup.api.rest.repository.PreguntaEvaluacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PreguntaEvaluacionService {

	@Autowired
	private PreguntaEvaluacionRepository preguntaEvaluacionRepository;
	
	@Autowired
	private EvaluacionRepository evaluacionRepository;

	
	public List<PreguntaEvaluacion> listarPreguntas(){
		return preguntaEvaluacionRepository.findAll();
	}
	
	public PreguntaEvaluacion buscarPorId(int id){
		return preguntaEvaluacionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No se encontro"));
	}
	
	public PreguntaEvaluacion guardarPreguntaEvaluacion(PreguntaEvaluacion pregEvaluacion) {
		if(pregEvaluacion.getTipoPregunta()==null) {
			throw new IllegalArgumentException("Tipo pregunta debe estar lleno");
		}
		if(pregEvaluacion.getTipoPregunta().length()>20) {
			throw new IllegalStateException("El Tipo Pregunta no puede tener mas de 20 carecteres");
		}
		if(pregEvaluacion.getEnunciado()==null) {
			throw new IllegalStateException("Enunciado debe estar lleno ");
		}
		if(pregEvaluacion.getPuntuacion()<=0) {
			throw new IllegalStateException("Puntuacion debe estar lleno ");
		}
		
		int idEvaluacion = pregEvaluacion.getEvaluacion().getIdEvaluacion();
        Evaluacion evaluacion = evaluacionRepository.findById(idEvaluacion)
        		.orElseThrow(() -> new IllegalArgumentException("El idEvaluacion no existe"));
        pregEvaluacion.setEvaluacion(evaluacion);
		pregEvaluacion.setEstado(1);
		
		return preguntaEvaluacionRepository.save(pregEvaluacion);
		
	}
	
	
	public PreguntaEvaluacion actualizarPreguntaEvaluacion(int id, PreguntaEvaluacion pregEvaluacion) {
	   
	    PreguntaEvaluacion preguntaExistente = preguntaEvaluacionRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("El id de la pregunta no existe"));

	    
	    if (pregEvaluacion.getTipoPregunta() == null) {
	        throw new IllegalArgumentException("Tipo pregunta debe estar lleno");
	    }
	    if (pregEvaluacion.getTipoPregunta().length() > 20) {
	        throw new IllegalStateException("El Tipo Pregunta no puede tener mas de 20 caracteres");
	    }
	    if (pregEvaluacion.getEnunciado() == null) {
	        throw new IllegalStateException("Enunciado debe estar lleno");
	    }
	    if (pregEvaluacion.getPuntuacion() <= 0) {
	        throw new IllegalStateException("Puntuación debe ser mayor a 0");
	    }

	    int idEvaluacion = pregEvaluacion.getEvaluacion().getIdEvaluacion();
	    Evaluacion evaluacion = evaluacionRepository.findById(idEvaluacion)
	        .orElseThrow(() -> new IllegalArgumentException("El idEvaluacion no existe"));
	    
	    preguntaExistente.setEvaluacion(evaluacion);
	    preguntaExistente.setTipoPregunta(pregEvaluacion.getTipoPregunta());
	    preguntaExistente.setEnunciado(pregEvaluacion.getEnunciado());
	    preguntaExistente.setPuntuacion(pregEvaluacion.getPuntuacion());
	    preguntaExistente.setEstado(preguntaExistente.getEstado());

	    return preguntaEvaluacionRepository.save(preguntaExistente);
	}

	
    public void eliminarPregEvaluacion(int id) {
        PreguntaEvaluacion pregEvaluacion = preguntaEvaluacionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id no encontrado"));
        
        if(pregEvaluacion.getEstado()==0) 
        {
        	throw new IllegalStateException("La evaluacion ya fue eliminada anteriormente.");
        }

        pregEvaluacion.setEstado(0);
        preguntaEvaluacionRepository.save(pregEvaluacion);
    }	
	

}
