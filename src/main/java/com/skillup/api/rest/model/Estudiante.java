package com.skillup.api.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "Estudiante") 
public class Estudiante {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "Id_Estudiante") 
    private int idEstudiante;
    
    @Column(name = "Id_Persona") 
    private int idPersona;
    
    @Column(name = "Nivel_Educativo") 
    @JsonProperty("nivel_educativo")
    private String nivelEducativo;
    
    @Column(name = "Estado")
    private int estado;

    public Estudiante() {
    }

    public Estudiante(int idEstudiante, int idPersona, String nivelEducativo, int estado) {
        this.idEstudiante = idEstudiante;
        this.idPersona = idPersona;
        this.nivelEducativo = nivelEducativo;
        this.estado = estado;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}