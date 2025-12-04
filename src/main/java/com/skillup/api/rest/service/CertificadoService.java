package com.skillup.api.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.dtos.CertificadoPorEstudiante;
import com.skillup.api.rest.repository.CertificadoPorEstudianteRepository;

@Service
public class CertificadoService {
	
	@Autowired
    private CertificadoPorEstudianteRepository certificadoRepository;

    public List<CertificadoPorEstudiante> listarCertificados(int idEstudiante) {
        return certificadoRepository.listarCertificadosPorEstudiante(idEstudiante);
    }
}
