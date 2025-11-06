package com.skillup.api.rest.controller;

// Importa todas las clases necesarias
import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List; // <-- ¡Asegúrate de importar List!

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "http://localhost:4200") // (Asumo puerto 4200 para Angular)
public class DocenteController {
    
    @Autowired
    private DocenteService docenteService;
    
    /**
     * Endpoint para LISTAR TODOS los docentes.
     * GET http://localhost:8888/api/docentes
     */
    @GetMapping
    public ResponseEntity<?> listarTodosDocentes() {
        try {
            List<Docente> docentes = docenteService.listarTodosDocentes();
            
            
            return ResponseEntity.ok(docentes);
            
        } catch (Exception e) {
            
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para OBTENER UN solo docente por ID.
     * GET http://localhost:8888/api/docentes/3
     */
    @GetMapping("/{idDocente}")
    public ResponseEntity<?> obtenerUno(@PathVariable int idDocente) {
        try {
            Docente docente = docenteService.obtenerDocente(idDocente);
            return ResponseEntity.ok(docente); // Devuelve el JSON del docente
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
   
}