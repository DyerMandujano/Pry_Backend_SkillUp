package com.skillup.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillup.api.rest.model.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

}
