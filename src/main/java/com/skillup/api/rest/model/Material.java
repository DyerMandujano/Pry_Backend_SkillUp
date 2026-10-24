package com.skillup.api.rest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Material")
    private int idMaterial;
    
    @Column(name = "Url_Material", length = 500)
    private String urlMaterial;
    
    @Column(name = "Nombre_Archivo", length = 200)
    private String nombreArchivo;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "Id_Leccion")
    private Leccion leccion;

    public Material() {
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getUrlMaterial() {
        return urlMaterial;
    }

    public void setUrlMaterial(String urlMaterial) {
        this.urlMaterial = urlMaterial;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Leccion getLeccion() {
        return leccion;
    }

    public void setLeccion(Leccion leccion) {
        this.leccion = leccion;
    }
}
