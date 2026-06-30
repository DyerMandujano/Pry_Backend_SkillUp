package com.skillup.api.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.repository.EstudianteRepository;

// --- ¡NUEVAS IMPORTACIONES! ---
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
// -----------------------------

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;
    

    @PersistenceContext
    private EntityManager entityManager;
    // -----------------------

    public Estudiante obtenerEstudiante(int idEstudiante) {
        
       
        Estudiante estudiante = entityManager.find(Estudiante.class, idEstudiante);
        
        if (estudiante == null) {
            throw new RuntimeException("Estudiante no encontrado con ID: " + idEstudiante);
        }
        return estudiante;
    }
    
    public List<Estudiante> listarEstudiantes() {
        

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(
            "dbo.SP_ListarEstudiante", 
            Estudiante.class  
        );


        return query.getResultList();
    }
    
    
    
    
}