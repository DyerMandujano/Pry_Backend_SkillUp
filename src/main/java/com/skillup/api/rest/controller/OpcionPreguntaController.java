package com.skillup.api.rest.controller;

import com.skillup.api.rest.model.OpcionPregunta;
import com.skillup.api.rest.service.OpcionPreguntaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opcionpreguntas")
public class OpcionPreguntaController {

    @Autowired
    private OpcionPreguntaService opcionPreguntaService;

    // 1. Listar todas las opciones de pregunta
    @GetMapping
    public List<OpcionPregunta> listarOpcionPreguntas() {
        return opcionPreguntaService.listarOpcionPreguntas();
    }

    // 2. Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<OpcionPregunta> buscarPorId(@PathVariable int id) {
        try {
            OpcionPregunta opcion = opcionPreguntaService.buscarPorId(id);
            return ResponseEntity.ok(opcion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Crear nueva opción de pregunta
    @PostMapping
    public ResponseEntity<OpcionPregunta> crearOpcionPregunta(@RequestBody OpcionPregunta opcionPregunta) {
        OpcionPregunta nuevaOpcion = opcionPreguntaService.guardarOpcionPregunta(opcionPregunta);
        return ResponseEntity.ok(nuevaOpcion);
    }

    // 4. Actualizar opción de pregunta
    @PutMapping("/{id}")
    public ResponseEntity<OpcionPregunta> actualizarOpcionPregunta(@PathVariable int id, @RequestBody OpcionPregunta datosNuevos) {
        try {
            OpcionPregunta opcionActualizada = opcionPreguntaService.actualizarOpcionPregunta(id, datosNuevos);
            return ResponseEntity.ok(opcionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Eliminar opción de pregunta (cambio de estado)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOpcionPregunta(@PathVariable int id) {
        try {
            opcionPreguntaService.eliminarOpcPregunta(id);
            return ResponseEntity.ok("Opción eliminada correctamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
