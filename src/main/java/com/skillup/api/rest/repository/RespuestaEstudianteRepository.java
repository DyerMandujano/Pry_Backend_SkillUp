package com.skillup.api.rest.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillup.api.rest.model.RespuestaEstudiante;

public interface RespuestaEstudianteRepository extends JpaRepository<RespuestaEstudiante, Integer> {
    @Query("SELECT r FROM RespuestaEstudiante r WHERE r.estudiante.idEstudiante = :idEstudiante AND r.opcionPregunta.pregEvaluacion.evaluacion.idSeccion = :idSeccion AND r.estado = 1")
    List<RespuestaEstudiante> findRespuestasByEstudianteYSeccion(@Param("idEstudiante") int idEstudiante, @Param("idSeccion") int idSeccion);

    @Query("SELECT r FROM RespuestaEstudiante r WHERE r.estudiante.idEstudiante = :idEstudiante AND r.opcionPregunta.pregEvaluacion.evaluacion.idSeccion IN (SELECT s.idSeccion FROM Seccion s WHERE s.idCurso = :idCurso) AND r.estado = 1")
    List<RespuestaEstudiante> findRespuestasByEstudianteYCurso(@Param("idEstudiante") int idEstudiante, @Param("idCurso") int idCurso);
}
