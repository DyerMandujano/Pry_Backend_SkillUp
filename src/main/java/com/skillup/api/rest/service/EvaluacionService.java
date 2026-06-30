package com.skillup.api.rest.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.repository.EvaluacionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;
        
    @PersistenceContext
    private EntityManager entityManager;
 
    
    
    public List<Evaluacion> MostrarTituloEvaluacionPorSeccion(int idSeccion) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_MostrarTituloEval", Evaluacion.class);
        query.registerStoredProcedureParameter("id_seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("id_seccion", idSeccion);
        return query.getResultList();
    }
    

    

       
    

}

