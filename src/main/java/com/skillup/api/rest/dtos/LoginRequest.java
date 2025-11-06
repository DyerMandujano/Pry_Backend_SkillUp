package com.skillup.api.rest.dtos;

import lombok.Data;

@Data // (Lombok) Crea getters y setters
public class LoginRequest {
    
    // Debe coincidir con el JSON que envía Angular
    // {
    //   "username": "usuario@mail.com",
    //   "contrasenia": "123456"
    // }
    
    private String username;
    private String contrasenia;
}