package com.skillup.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.dtos.EvaluacionCursoDTO;
import com.skillup.api.rest.service.EvaluacionCursoService;

@RestController
@RequestMapping("/api/evaluacion")
@CrossOrigin(origins = "http://localhost:4200") // para Angular
public class EvaluacionCursoController {

	 @Autowired
	    private EvaluacionCursoService evaluacionCursoService;

	    @GetMapping("/seccion/{idSeccion}")
	    public List<EvaluacionCursoDTO> listarEvaluacion(@PathVariable int idSeccion) {
	        return evaluacionCursoService.listarEvaluacion(idSeccion);
	    }
}
