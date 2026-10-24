package com.skillup.api.rest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Seccion")
public class Seccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Seccion")
    private Integer idSeccion;
	
    @Column(name = "Id_Curso")
    private Integer idCurso;

    @Column(name = "Nombre_Seccion")
    private String nombreSeccion;

    @Column(name = "Orden_Seccion")
    private Integer ordenSeccion;

    @Column(name = "Estado")
    private Integer estado;

    public Seccion() {
    }

    public Integer getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(Integer idSeccion) {
        this.idSeccion = idSeccion;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public Integer getOrdenSeccion() {
        return ordenSeccion;
    }

    public void setOrdenSeccion(Integer ordenSeccion) {
        this.ordenSeccion = ordenSeccion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
