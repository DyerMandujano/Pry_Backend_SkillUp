package com.skillup.api.rest.controller;
import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "http://localhost:4200") // para Angular
public class CursoController {
	  @Autowired
	    private CursoService cursoService;

	    @GetMapping
	    public List<Curso> listarCursos() {
	        return cursoService.listarCursos();
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


}
