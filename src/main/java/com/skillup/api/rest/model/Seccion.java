package com.skillup.api.rest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "Seccion")
public class Seccion {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Seccion")
    private Integer idSeccion;
	
	 @Column(name = "Id_Curso")
	    private Integer idCurso;

	    @Column(name = "Nombre_Seccion")
	    private String nombreSeccion;

	    @Column(name = "Orden_Seccion")
	    private Integer ordenSeccion;

	    @Column(name = "Estado")
	    private Integer estado;

    
}
