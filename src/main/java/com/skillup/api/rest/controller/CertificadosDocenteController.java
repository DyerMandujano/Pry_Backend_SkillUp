package com.skillup.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.dtos.CertificacionDocenteDTO;
import com.skillup.api.rest.service.CertificadosDocenteService;

@RestController
@RequestMapping("/api/certificados")
@CrossOrigin(origins = "http://localhost:4200")
public class CertificadosDocenteController {

	@Autowired
    private CertificadosDocenteService certificadosDocenteService;

    @GetMapping("/docente/{id}")
    public List<CertificacionDocenteDTO> listarCertificados(
            @PathVariable("id") int idDocente) {

        return certificadosDocenteService.listarPorDocente(idDocente);
    }
}
