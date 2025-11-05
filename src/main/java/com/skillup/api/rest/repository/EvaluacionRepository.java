package com.skillup.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillup.api.rest.model.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {
   
}

