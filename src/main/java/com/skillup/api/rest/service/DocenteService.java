package com.skillup.api.rest.service;

import java.util.List;
// -----------------------------

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.repository.DocenteRepository;
import com.skillup.api.rest.repository.PersonaRepository; // <-- Importar repo Persona
import com.skillup.api.rest.repository.UsuarioRepository; // <-- Importar repo Usuario

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;
    
    // --- ¡AÑADE ESTAS INYECCIONES! ---
    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    // ------------------------------------

    @PersistenceContext
    private EntityManager entityManager;


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
            Docente.class 
        );
        return query.getResultList();
    }

}