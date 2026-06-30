package com.skillup.api.rest.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

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

