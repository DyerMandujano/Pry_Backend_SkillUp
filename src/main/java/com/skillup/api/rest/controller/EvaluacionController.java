package com.skillup.api.rest.controller;

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.service.EvaluacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    // 1. Listar todas las evaluaciones
    @GetMapping
    public List<Evaluacion> listarEvaluaciones() {
        return evaluacionService.listarEvaluaciones();
    }

    // 2. Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> buscarPorId(@PathVariable int id) {
        Evaluacion evaluacion = evaluacionService.buscarPorId(id);
        if (evaluacion != null) {
            return ResponseEntity.ok(evaluacion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Evaluacion> crearEvaluacion(@RequestBody Evaluacion evaluacion) {
        Evaluacion nuevaEvaluacion = evaluacionService.guardarEvaluacion(evaluacion);
        return ResponseEntity.ok(nuevaEvaluacion);
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> actualizarEvaluacion(@PathVariable int id, @RequestBody Evaluacion datosNuevos) {
        try {
            Evaluacion evaluacionActualizada = evaluacionService.actualizarEvaluacion(id, datosNuevos);
            return ResponseEntity.ok(evaluacionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEvaluacion(@PathVariable int id) {
        try {
            evaluacionService.eliminarEvaluacion(id);
            return ResponseEntity.ok("Evaluación eliminada correctamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
