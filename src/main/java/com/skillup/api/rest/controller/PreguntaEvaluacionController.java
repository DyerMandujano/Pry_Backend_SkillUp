package com.skillup.api.rest.controller;

import com.skillup.api.rest.model.PreguntaEvaluacion;
import com.skillup.api.rest.service.PreguntaEvaluacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
public class PreguntaEvaluacionController {

    @Autowired
    private PreguntaEvaluacionService preguntaEvaluacionService;

    // 1. Listar todas las preguntas de evaluación
    @GetMapping
    public List<PreguntaEvaluacion> listarPreguntas() {
        return preguntaEvaluacionService.listarPreguntas();
    }

    // 2. Buscar una pregunta por ID
    @GetMapping("/{id}")
    public ResponseEntity<PreguntaEvaluacion> buscarPorId(@PathVariable int id) {
        try {
            PreguntaEvaluacion pregunta = preguntaEvaluacionService.buscarPorId(id);
            return ResponseEntity.ok(pregunta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Crear una nueva pregunta
    @PostMapping
    public ResponseEntity<PreguntaEvaluacion> crearPregunta(@RequestBody PreguntaEvaluacion pregunta) {
        try {
            PreguntaEvaluacion nuevaPregunta = preguntaEvaluacionService.guardarPreguntaEvaluacion(pregunta);
            return ResponseEntity.ok(nuevaPregunta);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 4. Actualizar una pregunta existente
    @PutMapping("/{id}")
    public ResponseEntity<PreguntaEvaluacion> actualizarPregunta(@PathVariable int id, @RequestBody PreguntaEvaluacion datosNuevos) {
        try {
            PreguntaEvaluacion preguntaActualizada = preguntaEvaluacionService.actualizarPreguntaEvaluacion(id, datosNuevos);
            return ResponseEntity.ok(preguntaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 5. Eliminar (cambiar estado a 0)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPregunta(@PathVariable int id) {
        try {
            preguntaEvaluacionService.eliminarPregEvaluacion(id);
            return ResponseEntity.ok("Pregunta eliminada correctamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
