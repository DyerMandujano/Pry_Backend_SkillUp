package com.skillup.api.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.model.Material;
import com.skillup.api.rest.repository.LeccionReporsitory; // Respetando tu nombre de variable

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class LeccionService {

    @Autowired
    private LeccionReporsitory leccionRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Leccion> listarLeccionesPorSeccion(int idSeccion) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_ListarLeccionesporSeccion", Leccion.class);
        query.registerStoredProcedureParameter("id_seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("id_seccion", idSeccion);
        return query.getResultList();
    }
    
    public Leccion obtenerLeccionPorId(int id) {
        return leccionRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public void insertarLeccion(Leccion leccion) {
        // 1. Enlazamos cada material con esta nueva lección antes de guardar
        if (leccion.getMateriales() != null && !leccion.getMateriales().isEmpty()) {
            for (Material material : leccion.getMateriales()) {
                material.setLeccion(leccion); 
            }
        }

        // 2. Guardamos usando el Repositorio JPA en lugar del SP
        leccionRepository.save(leccion);
    }
    
    @Transactional
    public void actualizarLeccion(Leccion leccionActualizada) {
        // 1. Buscamos la lección original en la base de datos
        Leccion leccionDB = leccionRepository.findById(leccionActualizada.getIdLeccion()).orElse(null);
        
        if (leccionDB != null) {
            // 2. Actualizamos los datos principales
            leccionDB.setNombreLeccion(leccionActualizada.getNombreLeccion());
            leccionDB.setDuracion(leccionActualizada.getDuracion());
            leccionDB.setOrdenLeccion(leccionActualizada.getOrdenLeccion());
            leccionDB.setEstado(leccionActualizada.getEstado());
            leccionDB.setUrlVideo(leccionActualizada.getUrlVideo());

            // 3. Sincronización segura de materiales
            if (leccionActualizada.getMateriales() != null) {
                // Eliminar los materiales que el usuario haya quitado en el formulario
                leccionDB.getMateriales().removeIf(mExistente -> 
                    leccionActualizada.getMateriales().stream()
                        .noneMatch(mNuevo -> mNuevo.getIdMaterial() > 0 && mNuevo.getIdMaterial() == mExistente.getIdMaterial())
                );

                // Actualizar materiales existentes o añadir nuevos
                for (Material mat : leccionActualizada.getMateriales()) {
                    // Ignorar entradas sin URL válida
                    if (mat.getUrlMaterial() == null || mat.getUrlMaterial().trim().isEmpty()) {
                        continue;
                    }

                    if (mat.getIdMaterial() > 0) {
                        // Actualizar material existente en la lista gestionada por JPA
                        leccionDB.getMateriales().stream()
                            .filter(m -> m.getIdMaterial() == mat.getIdMaterial())
                            .findFirst()
                            .ifPresent(m -> {
                                m.setNombreArchivo(mat.getNombreArchivo());
                                m.setUrlMaterial(mat.getUrlMaterial());
                            });
                    } else {
                        // Crear nuevo material
                        mat.setLeccion(leccionDB);
                        leccionDB.getMateriales().add(mat);
                    }
                }
            } else {
                leccionDB.getMateriales().clear();
            }

            // 4. Guardamos los cambios
            leccionRepository.save(leccionDB);
        }
    }
    
    @Transactional
    public void eliminarLeccion(int idLeccion) {
        // Usar el repositorio borra automáticamente los materiales hijos primero, 
        // evitando que SQL Server bloquee la acción.
        leccionRepository.deleteById(idLeccion);
    }
}