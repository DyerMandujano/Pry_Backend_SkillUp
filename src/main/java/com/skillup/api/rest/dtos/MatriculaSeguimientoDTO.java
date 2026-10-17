package com.skillup.api.rest.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MatriculaSeguimientoDTO {

    @JsonProperty("id_matricula")
    private int idMatricula;

    @JsonProperty("id_estudiante")
    private int idEstudiante;

    @JsonProperty("estudiante_nombre")
    private String estudianteNombre;

    @JsonProperty("estudiante_dni")
    private String estudianteDni;

    @JsonProperty("estudiante_username")
    private String estudianteUsername;

    @JsonProperty("id_curso")
    private int idCurso;

    @JsonProperty("nombre_curso")
    private String nombreCurso;

    @JsonProperty("fecha_matricula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaMatricula;

    private int estado;

    @JsonProperty("estado_descripcion")
    private String estadoDescripcion;

    @JsonProperty("porcentaje_avance")
    private int porcentajeAvance;

    public MatriculaSeguimientoDTO() {
    }
}
