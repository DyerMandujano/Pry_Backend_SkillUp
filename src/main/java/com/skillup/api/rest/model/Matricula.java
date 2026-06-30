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
}
