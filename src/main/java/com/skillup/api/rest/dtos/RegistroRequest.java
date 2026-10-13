package com.skillup.api.rest.dtos;

// Importa tus entidades del paquete model
import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data // (Lombok) Crea getters y setters
public class RegistroRequest {

    
    private Persona persona;
    private Usuario usuario;
    
    // Uno de estos dos será 'null', dependiendo del rol
    private Estudiante estudiante; 
    private Docente docente;     

    // Compatible si el cliente envía el DNI directamente en la raíz del JSON
    @JsonProperty("dni")
    @JsonAlias({"Dni", "DNI"})
    private String dni;
}
