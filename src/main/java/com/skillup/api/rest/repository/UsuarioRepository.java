package com.skillup.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.skillup.api.rest.model.Usuario;

@Repository // Le dice a Spring que esta es una interfaz de acceso a datos
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // 1. Reemplazo de tu método validarLogin(username, password)
    // Spring Data JPA crea el SQL: "SELECT * FROM Usuario WHERE Username = ?"
    // La validación de la contraseña la haremos en el @Service
    Usuario findByUsername(String username);

    // 2. Reemplazo de tu método verificarUsuarioExiste(username)
    // Spring Data JPA crea el SQL: "SELECT 1 FROM Usuario WHERE Username = ?"
    boolean existsByUsername(String username);
    

    @Modifying
    @Query("UPDATE Usuario u SET u.contrasenia = ?2 WHERE u.idUsuario = ?1")
    void actualizarPassword(Integer idUsuario, String nuevaPassword);
    
    // 4. Reemplazo de tu método eliminarUsuario(idUsuario)
    // Le decimos a Spring que esta consulta es un Stored Procedure (SP)
    @Procedure(name = "SP_DeleteEstudiante")
    void spDeleteEstudiante(Integer idUsuario);
}