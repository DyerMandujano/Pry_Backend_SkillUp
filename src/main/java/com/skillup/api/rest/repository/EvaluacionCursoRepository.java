package com.skillup.api.rest.repository;

import java.util.List;

import com.skillup.api.rest.dtos.EvaluacionCursoDTO;

public interface EvaluacionCursoRepository {
	List<EvaluacionCursoDTO> listarEvaluacionPorSeccion(int idSeccion);
}
