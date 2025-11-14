package com.skillup.api.rest.service;
import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.model.CursoMatricula;
import com.skillup.api.rest.repository.CursoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

	@Autowired
    private CursoRepository cursoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // 🔹 Listar todos los cursos
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }
    
    public List<Curso> listarCursosPorDocente(int idDocente) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_ListarCursosPorDoc", Curso.class);
        query.registerStoredProcedureParameter("idDocente", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("idDocente", idDocente);
        return query.getResultList();
    }
    
    public Curso obtenerCursoPorId(int id) {
        return cursoRepository.findById(id).orElse(null);
    }
    
    // 🔹 Insertar curso mediante SP
    public void insertarCurso(Curso curso) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_InsertCurso");

        query.registerStoredProcedureParameter("Id_Docente", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Id_Categoria", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Nombre_Curso", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Descripcion", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Resumen", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Nivel", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Duracion_Min", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Imagen_Curso2", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Imagen_Curso1", String.class, jakarta.persistence.ParameterMode.IN);

        query.setParameter("Id_Docente", curso.getIdDocente());
        query.setParameter("Id_Categoria", curso.getIdCategoria());
        query.setParameter("Nombre_Curso", curso.getNombreCurso());
        query.setParameter("Descripcion", curso.getDescripcion());
        query.setParameter("Resumen", curso.getResumen());
        query.setParameter("Nivel", curso.getNivel());
        query.setParameter("Duracion_Min", curso.getDuracionMin());
        query.setParameter("Imagen_Curso2", curso.getImagenCurso2());
        query.setParameter("Imagen_Curso1", curso.getImagenCurso1());

        query.execute();
    }
    
    
    public void actualizarCurso(Curso curso) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_UpdateCursoPorID");

        query.registerStoredProcedureParameter("Id_Curso", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Id_Categoria", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Nombre_Curso", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Descripcion", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Resumen", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Nivel", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Duracion_Min", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Imagen_Curso2", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Imagen_Curso1", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Estado", Integer.class, jakarta.persistence.ParameterMode.IN);

        query.setParameter("Id_Curso", curso.getIdCurso());
        query.setParameter("Id_Categoria", curso.getIdCategoria());
        query.setParameter("Nombre_Curso", curso.getNombreCurso());
        query.setParameter("Descripcion", curso.getDescripcion());
        query.setParameter("Resumen", curso.getResumen());
        query.setParameter("Nivel", curso.getNivel());
        query.setParameter("Duracion_Min", curso.getDuracionMin());
        query.setParameter("Imagen_Curso2", curso.getImagenCurso2());
        query.setParameter("Imagen_Curso1", curso.getImagenCurso1());
        query.setParameter("Estado", curso.getEstado());

        query.execute();
    }
    
    
    public void eliminarCurso(int idCurso) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_DeleteCursoPorID");

        query.registerStoredProcedureParameter("id", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("id", idCurso);

        query.execute();
    }
    
    //
    public List<CursoMatricula> listarCursosPorMatriculaEst(int idEstudiante) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_ListarCursosPorMatricula_Est");

        query.registerStoredProcedureParameter("id_estudiante", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("id_estudiante", idEstudiante);

        List<Object[]> results = query.getResultList();
        List<CursoMatricula> cursos = new java.util.ArrayList<>();

        for (Object[] row : results) {
        CursoMatricula curso = new CursoMatricula();
            curso.setIdEstudiante((Integer) row[0]);
            curso.setIdCurso((Integer) row[1]);
            curso.setNombreCurso((String) row[2]);
            curso.setNivel((String) row[3]);
            curso.setDuracionMin((Integer) row[4]);
            curso.setImagenCurso1((String) row[5]);
            curso.setImagenCurso2((String) row[6]);
            cursos.add(curso);
        }

        return cursos;
    }

}
