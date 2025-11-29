package com.skillup.api.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.dtos.EvaluacionCursoDTO;
import com.skillup.api.rest.repository.EvaluacionCursoRepository;

@Service
public class EvaluacionCursoService {

	@Autowired
    private EvaluacionCursoRepository evaluacionCursoRepository;

    public List<EvaluacionCursoDTO> listarEvaluacion(int idSeccion) {
    	
        return evaluacionCursoRepository.listarEvaluacionPorSeccion(idSeccion);
    }
}
