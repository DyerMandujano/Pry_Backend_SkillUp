package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class CertificacionDocenteDTO {
	private String docente;
    private Integer idCurso;
    private String nombreCurso;
    private String nivel;
    private String estudiante;
    private String codigoCertificado;
    private String fechaEmision;
}
