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

    // 🔹 NUEVA RELACIÓN DE UNO A MUCHOS PARA LOS MATERIALES DINÁMICOS
    @JsonManagedReference // Evita bucles infinitos al convertir a JSON
    @OneToMany(mappedBy = "leccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materiales;
}