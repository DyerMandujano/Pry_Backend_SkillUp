package com.skillup.api.rest.service;

import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// --- ¡NUEVAS IMPORTACIONES! ---
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery; // <-- ¡Importante para SP!
import java.util.List; // <-- ¡Importante para la Lista!
// -----------------------------

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    // --- ¡AÑADE ESTO! ---
    @PersistenceContext
    private EntityManager entityManager;
    // -----------------------


    public Docente obtenerDocente(int idDocente) {
        
        Docente docente = entityManager.find(Docente.class, idDocente);
        
        if (docente == null) {
            throw new RuntimeException("Docente no encontrado con ID: " + idDocente);
        }
        return docente;
    }
    

    public List<Docente> listarTodosDocentes() {
        

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(
            "dbo.SP_ListarDocente", 
            Docente.class  // 
        );


        return query.getResultList();
    }
}