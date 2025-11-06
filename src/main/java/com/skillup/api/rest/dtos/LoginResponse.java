package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class LoginResponse {

    private String username;
    private String rol;
    private String nombreCompleto;
    private String token;

    private int idUsuario;  
    private int idPersona;  
    
    
    private Integer idRolEspecifico; // El idDocente o idEstudiante

    public LoginResponse(String username, String rol, String nombreCompleto, String token, int idUsuario, int idPersona, Integer idRolEspecifico) {
        this.username = username;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.token = token;
        this.idUsuario = idUsuario;
        this.idPersona = idPersona;
        this.idRolEspecifico = idRolEspecifico;
    }
}