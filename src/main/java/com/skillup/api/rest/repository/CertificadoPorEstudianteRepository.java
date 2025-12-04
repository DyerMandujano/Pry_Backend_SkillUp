package com.skillup.api.rest.repository;

import java.util.List;

import com.skillup.api.rest.dtos.CertificadoPorEstudiante;

public interface CertificadoPorEstudianteRepository {

	List<CertificadoPorEstudiante> listarCertificadosPorEstudiante(int idEstudiante);
}
