package com.skillup.api.rest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Seccion")
public class Seccion {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "Id_Seccion")
    private int idSeccion;

	public int getIdSeccion() {
		return idSeccion;
	}

	public void setIdSeccion(int idSeccion) {
		this.idSeccion = idSeccion;
	}

    
}
