package com.skillup.api.rest.dtos;

import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistroRequest {

    private Persona persona;
    private Usuario usuario;
    
    // Uno de estos dos será 'null', dependiendo del rol
    private Estudiante estudiante; 
    private Docente docente;

    public RegistroRequest(Persona persona, Usuario usuario, Estudiante estudiante, Docente docente) {
        this.persona = persona;
        this.usuario = usuario;
        this.estudiante = estudiante;
        this.docente = docente;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}
