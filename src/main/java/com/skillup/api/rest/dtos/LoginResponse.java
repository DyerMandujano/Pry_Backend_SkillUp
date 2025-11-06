package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class LoginResponse {

    private String username;
    private String rol;
    private String nombreCompleto;
    private String token;

    // --- ¡¡CAMPOS NUEVOS Y ESENCIALES!! ---
    private int idUsuario;  // El ID de la tabla Usuario
    private int idPersona;  // El ID de la tabla Persona
    
    // (Opcional, pero muy útil)
    private Integer idRolEspecifico; // El idDocente o idEstudiante

    // Constructor actualizado
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