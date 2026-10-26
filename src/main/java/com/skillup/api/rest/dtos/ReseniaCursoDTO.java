package com.skillup.api.rest.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReseniaCursoDTO {
    private Integer idReseniaCurso;
    private Integer idCurso;
    private Integer idEstudiante;
    private String nombreEstudiante;
    private Integer calificacion;
    private String comentario;
    private Date fechaResenia;
    private Integer estado;

    public Integer getIdReseniaCurso() {
        return idReseniaCurso;
    }

    public void setIdReseniaCurso(Integer idReseniaCurso) {
        this.idReseniaCurso = idReseniaCurso;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFechaResenia() {
        return fechaResenia;
    }

    public void setFechaResenia(Date fechaResenia) {
        this.fechaResenia = fechaResenia;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
