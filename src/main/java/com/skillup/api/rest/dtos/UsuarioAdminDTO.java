package com.skillup.api.rest.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UsuarioAdminDTO {

    @JsonProperty("id_usuario")
    private int idUsuario;

    @JsonProperty("id_persona")
    private int idPersona;

    private String username;
    private String dni;
    private String nombres;
    private String apellidos;

    @JsonProperty("nombre_completo")
    private String nombreCompleto;

    private String genero;
    private String rol;
    private int estado;

    @JsonProperty("fecha_registro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaRegistro;

    public UsuarioAdminDTO() {
    }
}
