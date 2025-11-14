// 1. Asegúrate de que esté en el paquete correcto de tu API
package com.skillup.api.rest.model;

// 2. Importa las anotaciones de Jakarta Persistence (para Spring Boot 3)
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.Data;
import lombok.NoArgsConstructor; // Para el constructor vacío

/**
 * 
 */
@Data 
@NoArgsConstructor 
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


}