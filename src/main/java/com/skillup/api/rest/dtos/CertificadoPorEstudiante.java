package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class CertificadoPorEstudiante {
	private int idCurso;
    private String nombreCurso;
    private int duracionMin;
    private int idEstudiante;
    private String codigoCertificado;
    private String fechaEmision;
}
