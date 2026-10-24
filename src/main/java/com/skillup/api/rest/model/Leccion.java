package com.skillup.api.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Leccion")
public class Leccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Leccion")
    private int idLeccion;

    @Column(name = "Id_Seccion")
    private int idSeccion;

    @Column(name = "Nombre_Leccion", length = 50)
    private String nombreLeccion;

    @Column(name = "Duracion")
    private int duracion;

    @Column(name = "Orden_Leccion")
    private int ordenLeccion;

    @Column(name = "Estado")
    private int estado;
    
    @Column(name = "url_video", length = 500)
    private String urlVideo;

    @JsonManagedReference
    @OneToMany(mappedBy = "leccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materiales;

    public Leccion() {
    }

    public int getIdLeccion() {
        return idLeccion;
    }

    public void setIdLeccion(int idLeccion) {
        this.idLeccion = idLeccion;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getNombreLeccion() {
        return nombreLeccion;
    }

    public void setNombreLeccion(String nombreLeccion) {
        this.nombreLeccion = nombreLeccion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getOrdenLeccion() {
        return ordenLeccion;
    }

    public void setOrdenLeccion(int ordenLeccion) {
        this.ordenLeccion = ordenLeccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }
}
