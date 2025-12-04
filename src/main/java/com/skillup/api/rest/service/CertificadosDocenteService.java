package com.skillup.api.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.dtos.CertificacionDocenteDTO;
import com.skillup.api.rest.repository.CertificadosDocenteRepository;

@Service
public class CertificadosDocenteService {
	@Autowired
    private CertificadosDocenteRepository certificadosDocenteRepository;

    public List<CertificacionDocenteDTO> listarPorDocente(int idDocente) {
        return certificadosDocenteRepository.listarCertificadosPorDocente(idDocente);
    }
}
