package com.skillup.api.rest.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.service.LeccionService;

@RestController
@RequestMapping("/api/lecciones")
@CrossOrigin(origins = "*") 
public class LeccionController {

    @Autowired
    private LeccionService leccionService;

    @GetMapping("/seccion/{idSeccion}")
    public ResponseEntity<List<Leccion>> listarPorSeccion(@PathVariable int idSeccion) {
        List<Leccion> lecciones = leccionService.listarLeccionesPorSeccion(idSeccion);
        return new ResponseEntity<>(lecciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leccion> obtenerPorId(@PathVariable int id) {
        Leccion leccion = leccionService.obtenerLeccionPorId(id);
        if (leccion != null) {
            return new ResponseEntity<>(leccion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 🔹 MODIFICADO: Ahora recibe FormData (Leccion en JSON + Archivos físicos)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertar(
            @RequestPart("leccion") Leccion nuevaLeccion,
            @RequestPart(value = "archivos", required = false) List<MultipartFile> archivos) {
        try {
            guardarArchivosFisicos(nuevaLeccion, archivos);
            leccionService.insertarLeccion(nuevaLeccion);
            return new ResponseEntity<>("Lección creada correctamente.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear la lección: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 🔹 MODIFICADO: Ahora recibe FormData
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> actualizar(
            @PathVariable int id, 
            @RequestPart("leccion") Leccion leccionActualizada,
            @RequestPart(value = "archivos", required = false) List<MultipartFile> archivos) {
        try {
            leccionActualizada.setIdLeccion(id);
            guardarArchivosFisicos(leccionActualizada, archivos);
            leccionService.actualizarLeccion(leccionActualizada);
            return new ResponseEntity<>("Lección actualizada correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar la lección: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        try {
            leccionService.eliminarLeccion(id);
            return new ResponseEntity<>("Lección eliminada correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la lección: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 🛠️ FUNCIÓN UTILITARIA: Guarda los archivos en el disco duro y asigna la URL
    private void guardarArchivosFisicos(Leccion leccion, List<MultipartFile> archivos) {
        if (archivos != null && !archivos.isEmpty() && leccion.getMateriales() != null) {
            
            // Crea una carpeta llamada "uploads" en la raíz del backend si no existe
            String uploadDir = "uploads/"; 
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            int archivoIndex = 0;
            
            for (int i = 0; i < leccion.getMateriales().size(); i++) {
                // Solo procesamos si hay un archivo físico adjunto desde Angular
                if (leccion.getMateriales().get(i).getUrlMaterial() == null || leccion.getMateriales().get(i).getUrlMaterial().isEmpty()) {
                    if (archivoIndex < archivos.size()) {
                        MultipartFile file = archivos.get(archivoIndex);
                        try {
                            // Generamos un nombre único para evitar sobreescribir archivos con el mismo nombre
                            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                            Path filePath = Paths.get(uploadDir + fileName);
                            
                            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                            
                            // Guardamos la ruta relativa en el objeto que irá a SQL Server
                            leccion.getMateriales().get(i).setUrlMaterial(uploadDir + fileName);
                            archivoIndex++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}