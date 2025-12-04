package com.skillup.api.rest.repository;

import com.skillup.api.rest.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CertificadoRepository extends JpaRepository<Certificado, Integer> {

    // 1. Busca si ya existe el certificado (Tu query actual)
    @Query(value = "SELECT c.* FROM Certificado c " +
                   "INNER JOIN Matricula m ON c.Id_Matricula = m.Id_Matricula " +
                   "WHERE m.Id_Estudiante = :idEstudiante AND m.Id_Curso = :idCurso", 
                   nativeQuery = true)
    Optional<Certificado> findByEstudianteAndCurso(@Param("idEstudiante") int idEstudiante, 
                                                   @Param("idCurso") int idCurso);

    // 2. (NUEVO) Obtiene el ID de la matrícula basándose en el estudiante y curso
    @Query(value = "SELECT Id_Matricula FROM Matricula WHERE Id_Estudiante = :idEstudiante AND Id_Curso = :idCurso", nativeQuery = true)
    Integer findIdMatricula(@Param("idEstudiante") int idEstudiante, @Param("idCurso") int idCurso);
}