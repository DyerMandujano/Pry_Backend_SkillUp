package com.skillup.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillup.api.rest.model.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    boolean existsByIdEstudianteAndIdCurso(int idEstudiante, int idCurso);
    List<Matricula> findByIdEstudianteAndIdCurso(int idEstudiante, int idCurso);
    java.util.Optional<Matricula> findFirstByIdEstudianteAndIdCurso(int idEstudiante, int idCurso);
    List<Matricula> findByIdEstudiante(int idEstudiante);
}
