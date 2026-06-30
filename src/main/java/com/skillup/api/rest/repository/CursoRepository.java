package com.skillup.api.rest.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skillup.api.rest.model.Curso;

public interface CursoRepository extends JpaRepository <Curso, Integer>{

}
