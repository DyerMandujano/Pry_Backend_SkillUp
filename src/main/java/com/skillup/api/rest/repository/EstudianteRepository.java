package com.skillup.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.skillup.api.rest.model.Estudiante;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    // No necesita nada.
    
    Estudiante findByIdPersona(int idPersona);
}
