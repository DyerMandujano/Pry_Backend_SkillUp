package com.skillup.api.rest.controller;

// ... (tus importaciones @Autowired, ResponseEntity, @CrossOrigin, etc.)
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.service.EstudianteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping; // ¡Asegúrate de importar GET!
import org.springframework.web.bind.annotation.PathVariable; // ¡Importante!
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//ACA ESTA PARA ENCONTRAR A UN ESTUDIANTE
@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "http://localhost:4200")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;


    @GetMapping
    public ResponseEntity<?> listarTodos() {
       try {
            
            List<Estudiante> estudiantes = estudianteService.listarEstudiantes();
            
            
            return ResponseEntity.ok(estudiantes);
            
        } catch (Exception e) {
           
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- ¡¡NUEVO ENDPOINT!! ---
    // GET http://localhost:8888/api/estudiantes/15
    @GetMapping("/{idEstudiante}")
    public ResponseEntity<?> obtenerUno(@PathVariable int idEstudiante) {
        try {
            Estudiante estudiante = estudianteService.obtenerEstudiante(idEstudiante);
            return ResponseEntity.ok(estudiante); 
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
}