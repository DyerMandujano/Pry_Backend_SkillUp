package com.skillup.api.rest.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class MatriculaService {
	@PersistenceContext
    private EntityManager entityManager;

    // 🔹 Insertar matrícula mediante SP_InsertMatricula
    public void insertarMatricula(int idEstudiante, int idCurso) {
        StoredProcedureQuery query =
                entityManager.createStoredProcedureQuery("SP_InsertMatricula");

        query.registerStoredProcedureParameter("id_estudiante", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("id_curso", Integer.class, jakarta.persistence.ParameterMode.IN);

        query.setParameter("id_estudiante", idEstudiante);
        query.setParameter("id_curso", idCurso);

        query.execute();
    }
}
