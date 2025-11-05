package com.skillup.api.rest.controller;

import com.skillup.api.rest.model.RespuestaEstudiante;
import com.skillup.api.rest.service.RespuestaEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/respuestas")
public class RespuestaEstudianteController {

    @Autowired
    private RespuestaEstudianteService respuestaService;

    @GetMapping
    public List<RespuestaEstudiante> listarRespuestas() {
        return respuestaService.listarRespuestas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaEstudiante> buscarPorId(@PathVariable int id) {
        try {
            RespuestaEstudiante respuesta = respuestaService.buscarPorId(id);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<RespuestaEstudiante> crearRespuesta(@RequestBody RespuestaEstudiante respuesta) {
        RespuestaEstudiante nueva = respuestaService.guardarRespuesta(respuesta);
        return ResponseEntity.ok(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaEstudiante> actualizarRespuesta(
            @PathVariable int id,
            @RequestBody RespuestaEstudiante nueva) {
        try {
            RespuestaEstudiante actualizada = respuestaService.actualizarRespuesta(id, nueva);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRespuesta(@PathVariable int id) {
        try {
            respuestaService.eliminarRespuesta(id);
            return ResponseEntity.ok("Respuesta eliminada correctamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
