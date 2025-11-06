package com.skillup.api.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.skillup.api.rest.service.UsuarioService;
import com.skillup.api.rest.model.Persona;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios") // URL base: http://localhost:8080/api/usuarios
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // --- Endpoint para obtener el perfil de un usuario ---
    // GET http://localhost:8080/api/usuarios/perfil/5 
    @GetMapping("/perfil/{idPersona}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable int idPersona) {
        try {
            Persona persona = usuarioService.obtenerPerfil(idPersona);
            return ResponseEntity.ok(persona);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- Endpoint para actualizar el perfil de un usuario ---
    // PUT http://localhost:8080/api/usuarios/perfil/5
    @PutMapping("/perfil/{idPersona}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable int idPersona, @RequestBody Persona persona) {
        try {
            usuarioService.actualizarPerfil(idPersona, persona);
            return ResponseEntity.ok().body("Perfil actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // --- Endpoint para que un Admin liste todas las personas ---
    // GET http://localhost:8080/api/usuarios/listar-personas
    @GetMapping("/listar-personas")
    public ResponseEntity<List<Persona>> listarPersonas() {
        // (En un futuro, esto debería estar protegido para que solo 'Admin' pueda verlo)
        List<Persona> personas = usuarioService.listarTodasLasPersonas();
        return ResponseEntity.ok(personas);
    }
}
