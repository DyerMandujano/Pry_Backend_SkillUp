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
    
    // (Opcional) Si en el futuro quieres guardar el nombre visible del archivo
    @Column(name = "Nombre_Archivo", length = 200)
    private String nombreArchivo;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "Id_Leccion")
    private Leccion leccion;
}