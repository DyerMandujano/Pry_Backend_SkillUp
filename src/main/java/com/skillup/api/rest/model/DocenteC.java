package com.skillup.api.rest.model;

import lombok.Data;

@Data
public class DocenteC {

    private int idDocente;
    private String nombres;

    public DocenteC() {
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}
