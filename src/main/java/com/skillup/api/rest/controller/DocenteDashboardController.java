package com.skillup.api.rest.controller;

import com.skillup.api.rest.service.DocenteDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/docente-dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DocenteDashboardController {

    @Autowired
    private DocenteDashboardService docenteDashboardService;

    @GetMapping("/certificados-por-curso/{idDocente}")
    public ResponseEntity<?> getCertificadosPorCurso(@PathVariable int idDocente) {
        try {
            List<Map<String, Object>> result = docenteDashboardService.getCertificadosPorCurso(idDocente);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/ranking-cursos/{idDocente}")
    public ResponseEntity<?> getRankingCursos(@PathVariable int idDocente) {
        try {
            List<Map<String, Object>> result = docenteDashboardService.getRankingCursos(idDocente);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tendencia-certificados/{idDocente}")
    public ResponseEntity<?> getTendenciaCertificados(@PathVariable int idDocente) {
        try {
            List<Map<String, Object>> result = docenteDashboardService.getTendenciaCertificados(idDocente);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tendencia-matriculas/{idDocente}")
    public ResponseEntity<?> getTendenciaMatriculas(@PathVariable int idDocente) {
        try {
            List<Map<String, Object>> result = docenteDashboardService.getTendenciaMatriculas(idDocente);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estudiantes-por-curso/{idDocente}")
    public ResponseEntity<?> getEstudiantesPorCurso(@PathVariable int idDocente) {
        try {
            List<Map<String, Object>> result = docenteDashboardService.getEstudiantesPorCurso(idDocente);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}