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

    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public String getEstudianteDni() {
        return estudianteDni;
    }

    public void setEstudianteDni(String estudianteDni) {
        this.estudianteDni = estudianteDni;
    }

    public String getEstudianteUsername() {
        return estudianteUsername;
    }

    public void setEstudianteUsername(String estudianteUsername) {
        this.estudianteUsername = estudianteUsername;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public Date getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(Date fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public int getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(int porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }
}
