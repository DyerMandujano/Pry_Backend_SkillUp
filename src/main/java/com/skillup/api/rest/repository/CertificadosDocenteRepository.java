package com.skillup.api.rest.repository;

import java.util.List;

import com.skillup.api.rest.dtos.CertificacionDocenteDTO;

public interface CertificadosDocenteRepository {

	List<CertificacionDocenteDTO> listarCertificadosPorDocente(int idDocente);
}
