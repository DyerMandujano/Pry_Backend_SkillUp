package com.skillup.api.rest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Matricula")
    private int idMatricula;

    @Column(name = "Id_Estudiante")
    private int idEstudiante;

    @Column(name = "Id_Curso")
    private int idCurso;

    @Column(name = "Fecha_Matricula")
    private java.sql.Date fechaMatricula;

    @Column(name = "Estado")
    private int estado;

    @Column(name = "Intentos_Examen")
    private Integer intentosExamen;

    public Matricula() {
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

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public java.sql.Date getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(java.sql.Date fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Integer getIntentosExamen() {
        return intentosExamen;
    }

    public void setIntentosExamen(Integer intentosExamen) {
        this.intentosExamen = intentosExamen;
    }
}
