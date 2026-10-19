package com.skillup.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.dtos.RegistrarReseniaRequestDTO;
import com.skillup.api.rest.dtos.ReseniaCursoDTO;
import com.skillup.api.rest.service.ReseniaCursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cursos")
public class ReseniaCursoController {

    @Autowired
    private ReseniaCursoService reseniaCursoService;

    /**
     * HU20: Listar reseñas públicas de un curso
     */
    @GetMapping("/{idCurso}/resenias")
    public ResponseEntity<?> listarResenias(@PathVariable("idCurso") Integer idCurso) {
        try {
            List<ReseniaCursoDTO> lista = reseniaCursoService.listarReseniasPorCurso(idCurso);
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las reseñas: " + e.getMessage());
        }
    }

    /**
     * HU20 - RF-28: Publicar nueva reseña de un estudiante matriculado
     */
    @PostMapping("/{idCurso}/resenias")
    public ResponseEntity<?> registrarResenia(
            @PathVariable("idCurso") Integer idCurso,
            @Valid @RequestBody RegistrarReseniaRequestDTO request) {
        try {
            ReseniaCursoDTO nueva = reseniaCursoService.registrarResenia(idCurso, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al publicar la reseña: " + e.getMessage());
        }
    }
}
