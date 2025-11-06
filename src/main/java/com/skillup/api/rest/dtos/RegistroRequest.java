package com.skillup.api.rest.dtos;

// Importa tus entidades del paquete model
import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;

import lombok.Data;

@Data // (Lombok) Crea getters y setters
public class RegistroRequest {

    
    private Persona persona;
    private Usuario usuario;
    
    // Uno de estos dos será 'null', dependiendo del rol
    private Estudiante estudiante; 
    private Docente docente;     
}
