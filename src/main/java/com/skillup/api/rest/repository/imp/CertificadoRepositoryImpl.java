package com.skillup.api.rest.repository.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.skillup.api.rest.dtos.CertificadoPorEstudiante;
import com.skillup.api.rest.repository.CertificadoPorEstudianteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;


@Repository
public class CertificadoRepositoryImpl implements CertificadoPorEstudianteRepository {
	
	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CertificadoPorEstudiante> listarCertificadosPorEstudiante(int idEstudiante) {

        StoredProcedureQuery query =
                entityManager.createStoredProcedureQuery("SP_ListarCertificadoPorEstud");

        query.registerStoredProcedureParameter("id_estudiante", Integer.class,
                jakarta.persistence.ParameterMode.IN);

        query.setParameter("id_estudiante", idEstudiante);

        List<Object[]> resultados = query.getResultList();
        List<CertificadoPorEstudiante> lista = new ArrayList<>();

        for (Object[] row : resultados) {

            CertificadoPorEstudiante dto = new CertificadoPorEstudiante();
            dto.setIdCurso((Integer) row[0]);
            dto.setNombreCurso((String) row[1]);
            dto.setDuracionMin((Integer) row[2]);
            dto.setIdEstudiante((Integer) row[3]);
            dto.setCodigoCertificado((String) row[4]);
            dto.setFechaEmision(row[5] != null ? row[5].toString() : null);

            lista.add(dto);
        }

        return lista;
    }
}
