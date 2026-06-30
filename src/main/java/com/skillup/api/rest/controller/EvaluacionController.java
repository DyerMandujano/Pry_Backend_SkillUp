package com.skillup.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.service.EvaluacionService;

@RestController
@RequestMapping("/api/evaluaciones")
@CrossOrigin(origins = "http://localhost:4200") // para Angular
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;


    @GetMapping("/seccion/{idSeccion}")
    public List<Evaluacion> listarPorSeccion(@PathVariable int idSeccion) {
        return evaluacionService.MostrarTituloEvaluacionPorSeccion(idSeccion);
    }
    
}
