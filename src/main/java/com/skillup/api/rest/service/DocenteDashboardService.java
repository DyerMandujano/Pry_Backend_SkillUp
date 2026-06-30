package com.skillup.api.rest.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocenteDashboardService {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCertificadosPorCurso(int idDocente) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_Dashboard_CertificadosPorCurso");
        // Registrar el parámetro de forma explícita
        query.registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        query.setParameter("idDocente", idDocente);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getRankingCursos(int idDocente) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_Dashboard_RankingCursos");
        query.registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        query.setParameter("idDocente", idDocente);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getTendenciaCertificados(int idDocente) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_Dashboard_TendenciaCertificados");
        query.registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        query.setParameter("idDocente", idDocente);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getTendenciaMatriculas(int idDocente) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_Dashboard_TendenciaMatriculas");
        query.registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        query.setParameter("idDocente", idDocente);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getEstudiantesPorCurso(int idDocente) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_Dashboard_EstudiantesPorCurso");
        query.registerStoredProcedureParameter("idDocente", Integer.class, ParameterMode.IN);
        query.setParameter("idDocente", idDocente);
        return query.getResultList();
    }
}