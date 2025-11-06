package com.skillup.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.skillup.api.rest.model.Usuario;

@Repository 
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    

    Usuario findByUsername(String username);


    boolean existsByUsername(String username);
    

    @Modifying
    @Query("UPDATE Usuario u SET u.contrasenia = ?2 WHERE u.idUsuario = ?1")
    void actualizarPassword(Integer idUsuario, String nuevaPassword);
    
 
    @Procedure(name = "SP_DeleteEstudiante")
    void spDeleteEstudiante(Integer idUsuario);
}