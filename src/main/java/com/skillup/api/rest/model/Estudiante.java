// 1. Paquete de tu API
package com.skillup.api.rest.model;

// 2. Importaciones de Jakarta Persistence
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

// 3. (Recomendado) Importaciones de Lombok
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la tabla 'Estudiante' de la base de datos.
 */
@Data // (Lombok) Crea getters, setters, etc.
@NoArgsConstructor // (Lombok) Crea el constructor vacío
@Entity // (JPA) Marca como entidad
@Table(name = "Estudiante") // (JPA) Vincula a la tabla "Estudiante"
public class Estudiante {

    @Id // (JPA) Llave Primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (JPA) ID autoincremental
    @Column(name = "Id_Estudiante") // (JPA) Mapea a la columna "Id_Estudiante"
    private int idEstudiante;
    
    @Column(name = "Id_Persona") 
    private int idPersona;
    
    
    @Column(name = "Nivel_Educativo") 
    private String nivelEducativo;
    
    @Column(name = "Estado")
    private int estado;

    // (Si NO usas Lombok, deja todos tus getters y setters aquí)
}