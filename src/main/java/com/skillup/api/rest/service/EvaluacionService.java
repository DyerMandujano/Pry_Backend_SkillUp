package com.skillup.api.rest.service;


import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.repository.EvaluacionRepository;
import com.skillup.api.rest.repository.SeccionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;
    
    @Autowired
    private SeccionRepository seccionRepository; 
    //listar de evaluaciones 
 
    public List<Evaluacion> listarEvaluaciones() {
        return evaluacionRepository.findAll();
    }

    public Evaluacion buscarPorId(int id) {
        return evaluacionRepository.findById(id).orElseThrow(()->new IllegalArgumentException("No se encontro"));
    }

    
    public Evaluacion guardarEvaluacion(Evaluacion evaluacion) {
        if (evaluacion.getTitulo() == null || evaluacion.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio.");
        }
        if (evaluacion.getTitulo().length() > 50) {
            throw new IllegalArgumentException("El titulo no puede tener mas de 50 caracteres.");
        }
        if (evaluacion.getFechaCreacion() == null) {
            throw new IllegalArgumentException("Debe ingresar una fecha valida.");
        }

        // Verificar que la sección exista
        int idSeccion = evaluacion.getSeccion().getIdSeccion();
        Seccion seccionExistente = seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new IllegalArgumentException("La sección no existe"));

        evaluacion.setSeccion(seccionExistente);
        evaluacion.setEstado(1);

        return evaluacionRepository.save(evaluacion);
    }

    

    public Evaluacion actualizarEvaluacion(int id, Evaluacion datosNuevos) {
        Evaluacion evaluacion = evaluacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluacion no encontrada"));
        
        if (datosNuevos.getTitulo() == null || evaluacion.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio.");
        }
        if (datosNuevos.getTitulo().length() > 50) {
            throw new IllegalArgumentException("El titulo no puede tener mas de 50 caracteres.");
        }
        evaluacion.setTitulo(datosNuevos.getTitulo());
        
        if (datosNuevos.getFechaCreacion() == null) {
            throw new IllegalArgumentException("Debe ingresar una fecha valida.");
        }
        
        int idSeccion = datosNuevos.getSeccion().getIdSeccion();
        Seccion seccionExistente = seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new IllegalArgumentException("La sección no existe"));
        
        
        evaluacion.setTitulo(datosNuevos.getTitulo());
        evaluacion.setSeccion(seccionExistente);
        evaluacion.setFechaCreacion(datosNuevos.getFechaCreacion());
        evaluacion.setEstado(datosNuevos.getEstado());

        return evaluacionRepository.save(evaluacion);
    }

    public void eliminarEvaluacion(int id) {
        Evaluacion evaluacion = evaluacionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evaluacion no encontrada"));
        
        if(evaluacion.getEstado()==0) 
        {
        	throw new IllegalStateException("La evaluacion ya fue eliminada anteriormente.");
        }

        evaluacion.setEstado(0);
        evaluacionRepository.save(evaluacion);
    }	
    

    

       
    

}

