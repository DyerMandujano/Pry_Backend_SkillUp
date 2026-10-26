package com.skillup.api.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "Docente") 
public class Docente {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "Id_Docente") 
    private int idDocente; 

    @Column(name = "Id_Persona")
    private int idPersona; 

    @Column(name = "Especialidad")
    private String especialidad; 

    @Column(name = "Grado_Academico") 
    @JsonProperty("grado_academico")
    private String gradoAcademico; 

    @Column(name = "Firma")
    private String firma; 

    @Column(name = "Estado")
    private int estado;

    public Docente() {
    }

    public Docente(int idDocente, int idPersona, String especialidad, String gradoAcademico, String firma, int estado) {
        this.idDocente = idDocente;
        this.idPersona = idPersona;
        this.especialidad = especialidad;
        this.gradoAcademico = gradoAcademico;
        this.firma = firma;
        this.estado = estado;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getGradoAcademico() {
        return gradoAcademico;
    }

    public void setGradoAcademico(String gradoAcademico) {
        this.gradoAcademico = gradoAcademico;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}