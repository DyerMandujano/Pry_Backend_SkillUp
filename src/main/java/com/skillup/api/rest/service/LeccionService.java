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

            // 3. Vaciamos la lista actual (esto elimina de la BD los materiales que el usuario quitó)
            leccionDB.getMateriales().clear();
            
            // 4. Añadimos los materiales que vienen desde Angular
            if (leccionActualizada.getMateriales() != null) {
                for (Material mat : leccionActualizada.getMateriales()) {
                    mat.setLeccion(leccionDB);
                    leccionDB.getMateriales().add(mat);
                }
            }

            // 5. Guardamos los cambios
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