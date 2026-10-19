package com.skillup.api.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.service.MatriculaService;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

	    @Autowired
	    private MatriculaService matriculaService;

	    @Autowired
	    private com.skillup.api.rest.repository.MatriculaRepository matriculaRepository;

	    // 🔹 Insertar matrícula
	    @PostMapping("/insertar")
	    public String insertarMatricula(
	            @RequestParam int idEstudiante,
	            @RequestParam int idCurso) {

	        matriculaService.insertarMatricula(idEstudiante, idCurso);
	        return "Matrícula registrada correctamente";
	    }

	    // 🔹 Verificar si el estudiante está matriculado
	    @org.springframework.web.bind.annotation.GetMapping("/verificar")
	    public boolean verificarMatricula(
	            @RequestParam int idEstudiante,
	            @RequestParam int idCurso) {
	        return matriculaRepository.existsByIdEstudianteAndIdCurso(idEstudiante, idCurso);
	    }
}
