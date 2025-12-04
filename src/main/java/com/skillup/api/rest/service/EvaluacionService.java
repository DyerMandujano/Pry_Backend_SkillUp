package com.skillup.api.rest.service;


import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.repository.EvaluacionRepository;
import com.skillup.api.rest.repository.SeccionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

//En EvaluacionService.java
import jakarta.persistence.ParameterMode;
import com.skillup.api.rest.dtos.ResultadoVerificacionDTO;

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
    
    // NUEVO: usar SP_VerificarAprobacion
    public ResultadoVerificacionDTO verificarAprobacion(int idEstudiante, int idEvaluacion) {
        StoredProcedureQuery query = entityManager
            .createStoredProcedureQuery("SP_VerificarAprobacion")
            .registerStoredProcedureParameter("Id_Estudiante", Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter("Id_Evaluacion", Integer.class, ParameterMode.IN)
            .setParameter("Id_Estudiante", idEstudiante)
            .setParameter("Id_Evaluacion", idEvaluacion);

        Object[] result = (Object[]) query.getSingleResult();

        ResultadoVerificacionDTO dto = new ResultadoVerificacionDTO();
        dto.setTotalPreguntas((Integer) result[0]);
        dto.setCorrectas((Integer) result[1]);
        dto.setResultado((String) result[2]);

        return dto;
    }

    

       
    

}

