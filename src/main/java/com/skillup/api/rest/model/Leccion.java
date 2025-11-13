package com.skillup.api.rest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	    @Column(name = "Material_1", columnDefinition = "TEXT")
	    private String material1;

	    @Column(name = "Material_2", columnDefinition = "TEXT")
	    private String material2;

	    @Column(name = "Orden_Leccion")
	    private int ordenLeccion;

	    @Column(name = "Estado")
	    private int estado;
}
