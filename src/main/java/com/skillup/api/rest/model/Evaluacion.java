package com.skillup.api.rest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "Id_Evaluacion")
    private int idEvaluacion;

    @Column(name = "Id_Seccion")
    private int idSeccion;
    
    @Column(name = "Titulo", length = 50)
    private String titulo;

    @Column(name = "Fecha_Creacion")
    private LocalDate fechaCreacion;

    @Column(name = "Estado")
    private int estado;


}

