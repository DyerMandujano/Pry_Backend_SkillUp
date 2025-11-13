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

import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.service.SeccionService;

@RestController
@RequestMapping("/api/secciones")
@CrossOrigin(origins = "http://localhost:4200") // para Angular
public class SeccionController {

	@Autowired
    private SeccionService seccionService;

	@GetMapping("/curso/{idCurso}")
    public List<Seccion> listarPorCurso(@PathVariable int idCurso) {
        return seccionService.listarSeccionesPorCurso(idCurso);
    }
	
	@GetMapping("/seccion/{id}")
    public Seccion obtenerSeccionPorId(@PathVariable int id) {
        return seccionService.obtenerSeccionPorId(id);
    }
	
	@PostMapping
    public String insertarSeccion(@RequestBody Seccion seccion) {
        seccionService.insertarSeccion(seccion);
        return "✅ Sección registrada correctamente mediante procedimiento almacenado.";
    }
	
	//Actualizar seccion por ID 
    @PutMapping("/seccion/{id}")
    public String actualizarSeccion(@PathVariable int id, @RequestBody Seccion seccion) {
        seccion.setIdSeccion(id); // asegurar que coincida el ID del body con el de la URL
        seccionService.actualizarSeccion(seccion);
        return "✅ Sección actualizada correctamente mediante procedimiento almacenado.";
    }
    
	 //Eliminar seccion por ID
	    @DeleteMapping("/seccion/{id}")
	    public String eliminarSeccion(@PathVariable int id) {
	    	seccionService.eliminarSeccion(id);
	        return "🗑️ Sección eliminada correctamente (Estado cambiado a 0).";
	    }
}
