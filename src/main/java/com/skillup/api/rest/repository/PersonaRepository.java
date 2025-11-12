package com.skillup.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.skillup.api.rest.model.Persona;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    
   
    // 1. Reemplazo de SP_ListarPersonas
    @Procedure(name = "dbo.SP_ListarPersonas")
    List<Persona> spListarPersonas();

    // 2. Reemplazo de SP_BuscarPersonaPorID
    //    findById() ya hace esto, pero si tu SP es más complejo, lo puedes usar.
    @Procedure(name = "dbo.SP_BuscarPersonaPorID")
    //Persona spBuscarPersonaPorID(Integer idPersona);
    
    // 3. Reemplazo de SP_UpdatePersonaPorID
//@Procedure(name = "dbo.SP_UpdatePersonaPorID")
void spUpdatePersonaPorID(
    @Param("id") Integer idPersona, 
    @Param("nombres") String nombres, 
    @Param("apellidos") String apellidos, 
    @Param("fecha_nac") Date fechaNac, 
    @Param("genero") String genero,
    @Param("estado") Integer estado
);
}