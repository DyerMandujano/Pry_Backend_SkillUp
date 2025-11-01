package com.skillup.api.rest.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Curso")
public class Curso {
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Curso")
    private Integer idCurso;

    @Column(name = "Id_Docente")
    private Integer idDocente;

    @Column(name = "Id_Categoria")
    private Integer idCategoria;

    @Column(name = "Nombre_Curso", length = 100)
    private String nombreCurso;

    @Column(name = "Descripcion", columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @Column(name = "Resumen", length = 200)
    private String resumen;

    @Column(name = "Nivel", length = 10)
    private String nivel;

    @Column(name = "Duracion_Min")
    private Integer duracionMin;

    @Column(name = "Fecha_Publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "Imagen_Curso1", columnDefinition = "VARCHAR(MAX)")
    private String imagenCurso1;

    @Column(name = "Imagen_Curso2", columnDefinition = "VARCHAR(MAX)")
    private String imagenCurso2;

    @Column(name = "Estado")
    private Integer estado;
    
    
}
