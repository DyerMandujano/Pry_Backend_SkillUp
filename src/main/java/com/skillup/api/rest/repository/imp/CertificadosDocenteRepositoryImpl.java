package com.skillup.api.rest.repository.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.skillup.api.rest.dtos.CertificacionDocenteDTO;
import com.skillup.api.rest.repository.CertificadosDocenteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Repository
public class CertificadosDocenteRepositoryImpl implements CertificadosDocenteRepository {
	
	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CertificacionDocenteDTO> listarCertificadosPorDocente(int idDocente) {

        StoredProcedureQuery query =
                entityManager.createStoredProcedureQuery("SP_CertificadosPorDoc");

        query.registerStoredProcedureParameter("id_docente", Integer.class,
                jakarta.persistence.ParameterMode.IN);

        query.setParameter("id_docente", idDocente);

        List<Object[]> resultados = query.getResultList();
        List<CertificacionDocenteDTO> lista = new ArrayList<>();

        for (Object[] row : resultados) {

        	CertificacionDocenteDTO dto = new CertificacionDocenteDTO();

            dto.setDocente((String) row[0]);
            dto.setIdCurso((Integer) row[1]);
            dto.setNombreCurso((String) row[2]);
            dto.setNivel((String) row[3]);
            dto.setEstudiante((String) row[4]);
            dto.setCodigoCertificado((String) row[5]);
            dto.setFechaEmision(row[6] != null ? row[6].toString() : null);

            lista.add(dto);
        }

        return lista;
    }
}
