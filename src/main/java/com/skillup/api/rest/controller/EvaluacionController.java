package com.skillup.api.rest.controller;

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.service.EvaluacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
