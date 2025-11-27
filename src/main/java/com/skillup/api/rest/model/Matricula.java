package com.skillup.api.rest.model;

import jakarta.persistence.*;
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
