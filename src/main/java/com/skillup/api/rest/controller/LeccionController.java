package com.skillup.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.service.LeccionService;

@RestController
@RequestMapping("/api/lecciones")
@CrossOrigin(origins = "http://localhost:4200")
public class LeccionController {
	
	@Autowired
    private LeccionService leccionService;
	
	
	@GetMapping("/seccion/{idSeccion}")
    public List<Leccion> listarPorSeccion(@PathVariable int idSeccion) {
        return leccionService.listarLeccionesPorSeccion(idSeccion);
    }
	
	@GetMapping("/leccion/{id}")
    public Leccion obtenerLeccionPorId(@PathVariable int id) {
        return leccionService.obtenerLeccionPorId(id);
    }
	
	
	@PostMapping
    public String insertarLeccion(@RequestBody Leccion leccion) {
        leccionService.insertarLeccion(leccion);
        return "✅ Lección registrada correctamente mediante procedimiento almacenado.";
    }
	
	@PutMapping("/leccion/{id}")
	public String actualizarLeccion(@PathVariable int id, @RequestBody Leccion leccion) {
	    leccion.setIdLeccion(id); // asegura que se use el ID de la URL
	    leccionService.actualizarLeccion(leccion);
	    return "✅ Lección actualizada correctamente mediante procedimiento almacenado.";
	}
	
	//Eliminar leccion por ID
    @DeleteMapping("/leccion/{id}")
    public String eliminarSeccion(@PathVariable int id) {
    	leccionService.eliminarLeccion(id);
        return "🗑️ Lección eliminada correctamente (Estado cambiado a 0).";
    }
}
