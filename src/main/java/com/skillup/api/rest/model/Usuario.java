// 1. Paquete de tu API
package com.skillup.api.rest.model;

// 2. Importaciones de Jakarta Persistence
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

// 3. Importa java.util.Date
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la tabla 'Usuario' de la base de datos.
 */
@Data 
@NoArgsConstructor 
@Entity 
@Table(name = "Usuario") 
public class Usuario {

    @Id // (JPA) Llave Primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (JPA) ID autoincremental
    @Column(name = "Id_Usuario")
    private int idUsuario;
    
    @Column(name = "Id_Persona")
    private int idPersona;
    
    @Column(name = "Username")
    private String username;
    
    @Column(name = "Contrasenia")
    private String contrasenia;
    
    @Column(name = "Rol")
    private String rol;
    
    
    @Column(name = "Fec_Registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRegistro;

}