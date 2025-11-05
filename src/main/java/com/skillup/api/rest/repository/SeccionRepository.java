package com.skillup.api.rest.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.skillup.api.rest.model.Evaluacion;
import com.skillup.api.rest.model.Seccion;

public interface SeccionRepository extends JpaRepository<Seccion, Integer> {

}
