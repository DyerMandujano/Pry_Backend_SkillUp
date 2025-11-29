package com.skillup.api.rest.repository.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.skillup.api.rest.dtos.EvaluacionCursoDTO;
import com.skillup.api.rest.repository.EvaluacionCursoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Repository
public class EvaluacionCursoRepositoryImpl implements EvaluacionCursoRepository {

	 @PersistenceContext
	    private EntityManager entityManager;

	    @Override
	    public List<EvaluacionCursoDTO> listarEvaluacionPorSeccion(int idSeccion) {

	        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_ListarEvaluacionCurso");

	        query.registerStoredProcedureParameter("Id_Seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.setParameter("Id_Seccion", idSeccion);

	        List<Object[]> resultados = query.getResultList();
	        List<EvaluacionCursoDTO> lista = new ArrayList<>();

	        for (Object[] row : resultados) {
	            EvaluacionCursoDTO dto = new EvaluacionCursoDTO();
	            dto.setIdPregunta((Integer) row[0]);
	            dto.setEnunciado((String) row[1]);

	            dto.setOpcion1((String) row[2]);
	            dto.setOpcion2((String) row[3]);
	            dto.setOpcion3((String) row[4]);

	            dto.setIdOp1(row[5] != null ? ((Number) row[5]).intValue() : null);
	            dto.setIdOp2(row[6] != null ? ((Number) row[6]).intValue() : null);
	            dto.setIdOp3(row[7] != null ? ((Number) row[7]).intValue() : null);

	            lista.add(dto);
	        }

	        return lista;
	    }
}
