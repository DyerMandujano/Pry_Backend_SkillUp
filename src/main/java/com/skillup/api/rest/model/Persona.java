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

// 4. (Recomendado) Importaciones de Lombok
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la tabla 'Persona' de la base de datos.
 */
@Data 
@NoArgsConstructor 
@Entity 
@Table(name = "Persona") 
public class Persona {

    @Id // (JPA) Llave Primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "Id_Persona")
    private int idPersona;
    
    @Column(name = "Nombres")
    private String nombres;
    
    @Column(name = "Apellidos")
    private String apellidos;
    
    
    @Column(name = "Fecha_de_nacimiento")
    @Temporal(TemporalType.DATE) 
    private Date fechaDeNacimiento;
    
    @Column(name = "Genero")
    private String genero;
    
    @Column(name = "Estado")
    private int estado;

    
}
