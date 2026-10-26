package com.skillup.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.dtos.EvaluacionCursoDTO;
import com.skillup.api.rest.service.EvaluacionCursoService;

@RestController
@RequestMapping("/api/evaluacion")
public class EvaluacionCursoController {

	 @Autowired
	    private EvaluacionCursoService evaluacionCursoService;

	    @GetMapping("/seccion/{idSeccion}")
	    public List<EvaluacionCursoDTO> listarEvaluacion(@PathVariable int idSeccion) {
	        return evaluacionCursoService.listarEvaluacion(idSeccion);
	    }

	    @GetMapping("/seccion/{idSeccion}/estado")
	    public org.springframework.http.ResponseEntity<?> obtenerEstadoEvaluacion(
	            @PathVariable int idSeccion,
	            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "0") int idEstudiante) {
	        try {
	            com.skillup.api.rest.dtos.EvaluacionEstadoDTO estado = evaluacionCursoService.obtenerEstadoEvaluacion(idSeccion, idEstudiante);
	            return org.springframework.http.ResponseEntity.ok(estado);
	        } catch (Exception e) {
	            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }

	    @org.springframework.web.bind.annotation.PostMapping("/seccion/{idSeccion}/enviar")
	    public org.springframework.http.ResponseEntity<?> enviarEvaluacion(
	            @PathVariable int idSeccion,
	            @org.springframework.web.bind.annotation.RequestBody com.skillup.api.rest.dtos.EvaluacionSubmitDTO submitDTO) {
	        try {
	            com.skillup.api.rest.dtos.EvaluacionResultadoDTO resultado = evaluacionCursoService.evaluarYGuardarRespuestas(idSeccion, submitDTO);
	            return org.springframework.http.ResponseEntity.ok(resultado);
	        } catch (Exception e) {
	            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }
}
