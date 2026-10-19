package com.skillup.api.rest.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.model.CursoMatricula;
import com.skillup.api.rest.model.CursoNoMatricula;
import com.skillup.api.rest.service.CursoService;


@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = {"http://192.168.56.1:4200", "http://localhost:4200"}) // para Angular
public class CursoController {
	  @Autowired
	    private CursoService cursoService;

	    @GetMapping
	    public List<Curso> listarCursos() {
	        return cursoService.listarCursos();
	    }
	    
	    @GetMapping("/{id}")
	    public Curso obtenerCursoPorId(@PathVariable int id) {
	        return cursoService.obtenerCursoPorId(id);
	    }
	    
	    @GetMapping("/docente/{idDocente}")
	    public List<Curso> listarPorDocente(@PathVariable int idDocente) {
	        return cursoService.listarCursosPorDocente(idDocente);
	    }

	    @PostMapping
	    public String insertarCurso(@RequestBody Curso curso) {
	        cursoService.insertarCurso(curso);
	        return "✅ Curso registrado correctamente mediante procedimiento almacenado.";
	    }
	    
	    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<?> subirImagenCurso(@RequestParam("archivo") MultipartFile archivo) {
	        try {
	            if (archivo == null || archivo.isEmpty()) {
	                return ResponseEntity.badRequest().body("El archivo está vacío.");
	            }
	            String uploadDir = "uploads/";
	            File dir = new File(uploadDir);
	            if (!dir.exists()) {
	                dir.mkdirs();
	            }

	            String originalName = archivo.getOriginalFilename();
	            if (originalName == null || originalName.trim().isEmpty()) {
	                originalName = "curso_" + System.currentTimeMillis() + ".jpg";
	            }
	            String safeName = originalName.replaceAll("\\s+", "_");
	            Path filePath = Paths.get(uploadDir + safeName);
	            Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	            Map<String, String> response = new HashMap<>();
	            response.put("fileName", safeName);
	            response.put("url", "/uploads/" + safeName);
	            return ResponseEntity.ok(response);
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Error al subir imagen: " + e.getMessage());
	        }
	    }
	    
	 // 🔹 Actualizar curso por ID 
	    @PutMapping("/{id}")
	    public String actualizarCurso(@PathVariable int id, @RequestBody Curso curso) {
	        curso.setIdCurso(id); // asegurar que coincida el ID del body con el de la URL
	        cursoService.actualizarCurso(curso);
	        return "✅ Curso actualizado correctamente mediante procedimiento almacenado.";
	    }
	    
	 // 🔹 Eliminar curso por ID (SP_DeleteCursoPorID)
	    @DeleteMapping("/{id}")
	    public String eliminarCurso(@PathVariable int id) {
	        cursoService.eliminarCurso(id);
	        return "🗑️ Curso eliminado correctamente (Estado cambiado a 0).";
	    }
	    
	 // 🔹 Listar cursos matriculados por estudiante
	    @GetMapping("/matricula/estudiante/{idEstudiante}")
	    public List<CursoMatricula> listarCursosPorEstudiante(@PathVariable int idEstudiante) {
	        return cursoService.listarCursosPorMatriculaEst(idEstudiante);
	    }
	    
	    // 🔹 Listar cursos NO matriculados por estudiante
	    @GetMapping("/no-matricula/{idEstudiante}")
	    public List<CursoNoMatricula> listarCursosNoMatriculaEstudiante(@PathVariable int idEstudiante) {
	        return cursoService.listarCursosSinMatricularse(idEstudiante);
	    }


}
