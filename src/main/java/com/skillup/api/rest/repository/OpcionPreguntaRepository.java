package com.skillup.api.rest.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillup.api.rest.model.OpcionPregunta;

public interface OpcionPreguntaRepository extends JpaRepository<OpcionPregunta, Integer> {
    @Query("SELECT o FROM OpcionPregunta o WHERE o.pregEvaluacion.idPregunta = :idPregunta AND o.estado = 1")
    List<OpcionPregunta> findByPregunta(@Param("idPregunta") int idPregunta);

    @Query("SELECT o FROM OpcionPregunta o WHERE o.pregEvaluacion.idPregunta = :idPregunta AND UPPER(o.esCorrecta) = 'SI' AND o.estado = 1")
    Optional<OpcionPregunta> findCorrectaByPregunta(@Param("idPregunta") int idPregunta);
}
