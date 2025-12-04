package com.skillup.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.dtos.CertificadoPorEstudiante;
import com.skillup.api.rest.service.CertificadoService;

@RestController
@RequestMapping("/api/certificados")
@CrossOrigin(origins = "http://localhost:4200")
public class CertificadoController {
	@Autowired
    private CertificadoService certificadoService;

    @GetMapping("/estudiante/{id}")
    public List<CertificadoPorEstudiante> listarCertificadosPorEstudiante(
            @PathVariable("id") int idEstudiante) {

        return certificadoService.listarCertificados(idEstudiante);
    }
}
