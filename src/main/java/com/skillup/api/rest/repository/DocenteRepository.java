package com.skillup.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.skillup.api.rest.model.Docente;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    
    // Reemplazo de tu método BuscarID(id)
    // "nativeQuery = true" nos deja usar SQL puro de tu BD
    @Query(value = "SELECT Id_Docente FROM Docente WHERE Id_Persona = ?1", nativeQuery = true)
    Integer findIdDocenteByIdPersona(Integer idPersona);
}