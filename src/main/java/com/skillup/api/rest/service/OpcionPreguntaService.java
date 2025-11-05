package com.skillup.api.rest.service;

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.model.OpcionPregunta;
import com.skillup.api.rest.model.PreguntaEvaluacion;
import com.skillup.api.rest.repository.OpcionPreguntaRepository;
import com.skillup.api.rest.repository.PreguntaEvaluacionRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OpcionPreguntaService {

	@Autowired
	private OpcionPreguntaRepository opcionPreguntaRepository;
	
	@Autowired 
	private PreguntaEvaluacionRepository preguntaEvaluacionRepository;
	
	public List<OpcionPregunta> listarOpcionPreguntas(){
		return opcionPreguntaRepository.findAll();
	}
	
	public OpcionPregunta buscarPorId(int id) {
		return opcionPreguntaRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Opcion no encontrada"));

	}
	
	public OpcionPregunta guardarOpcionPregunta(OpcionPregunta opcionPregunta) {
			if(opcionPregunta.getOpcion()==null) {
				throw new IllegalArgumentException("Campo opcion debe estar lleno");
			}
			if(opcionPregunta.getOpcion().length()>100) {
				throw new IllegalStateException("El campo opcion no puede tener mas de 100 carecteres");
			}
			if(opcionPregunta.getEsCorrecta()==null) {
				throw new IllegalStateException("Escorrecto debe estar lleno ");
			}

			if(opcionPregunta.getEsCorrecta().length()>20) {
				throw new IllegalStateException("El campo esCorrecto no puede tener mas de 20 carecteres");
			}

			int idPregunta = opcionPregunta.getPregEvaluacion().getIdPregunta();
	        PreguntaEvaluacion pregunta = preguntaEvaluacionRepository.findById(idPregunta)
	        		.orElseThrow(() -> new IllegalArgumentException("El idPregunta no existe"));
	        opcionPregunta.setPregEvaluacion(pregunta);
			opcionPregunta.setEstado(1);
			
			return opcionPreguntaRepository.save(opcionPregunta);
		
	}
	
	public OpcionPregunta actualizarOpcionPregunta (int id,OpcionPregunta opcionPregunta) {
		OpcionPregunta opcionExistente=opcionPreguntaRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("idOpcion no encontrado") );
		
		if(opcionPregunta.getOpcion()==null) {
			throw new IllegalArgumentException("Campo opcion debe estar lleno");
		}
		if(opcionPregunta.getOpcion().length()>100) {
			throw new IllegalStateException("El campo opcion no puede tener mas de 100 carecteres");
		}
		if(opcionPregunta.getEsCorrecta()==null) {
			throw new IllegalStateException("Escorrecto debe estar lleno ");
		}

		if(opcionPregunta.getEsCorrecta().length()>20) {
			throw new IllegalStateException("El campo esCorrecto no puede tener mas de 20 carecteres");
		}

		int idPregunta = opcionPregunta.getPregEvaluacion().getIdPregunta();
        PreguntaEvaluacion pregunta = preguntaEvaluacionRepository.findById(idPregunta)
        		.orElseThrow(() -> new IllegalArgumentException("El idPregunta no existe"));

        opcionExistente.setOpcion(opcionPregunta.getOpcion());
        opcionExistente.setEsCorrecta(opcionPregunta.getEsCorrecta());
        opcionExistente.setPregEvaluacion(pregunta);
		opcionExistente.setEstado(opcionPregunta.getEstado());
		
		return opcionPreguntaRepository.save(opcionExistente);
	}
	
	
	public void eliminarOpcPregunta(int id) 
	{
		OpcionPregunta opcionExistente=opcionPreguntaRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("idOpcion no encontrado") );
		
		if(opcionExistente.getEstado()==0) 
		{
			throw new IllegalStateException("Esta opcion ya fue eliminada antes");
		}
		
		opcionExistente.setEstado(0);
		opcionPreguntaRepository.save(opcionExistente);
	}
		
	
	
}
