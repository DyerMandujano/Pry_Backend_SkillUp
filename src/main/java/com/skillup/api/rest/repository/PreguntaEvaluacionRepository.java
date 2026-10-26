package com.skillup.api.rest.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillup.api.rest.model.PreguntaEvaluacion;

public interface PreguntaEvaluacionRepository extends JpaRepository<PreguntaEvaluacion, Integer> {
    @Query("SELECT p FROM PreguntaEvaluacion p WHERE p.evaluacion.idSeccion = :idSeccion AND p.estado = 1")
    List<PreguntaEvaluacion> findBySeccion(@Param("idSeccion") int idSeccion);

    @Query("SELECT p FROM PreguntaEvaluacion p WHERE p.evaluacion.idSeccion IN (SELECT s.idSeccion FROM Seccion s WHERE s.idCurso = :idCurso) AND p.estado = 1")
    List<PreguntaEvaluacion> findByCurso(@Param("idCurso") int idCurso);
}
