package com.skillup.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.api.rest.model.ReseniaCurso;

@Repository
public interface ReseniaCursoRepository extends JpaRepository<ReseniaCurso, Integer> {
    List<ReseniaCurso> findByIdCursoAndEstadoOrderByFechaReseniaDesc(Integer idCurso, Integer estado);
    List<ReseniaCurso> findByIdCursoOrderByFechaReseniaDesc(Integer idCurso);
}
