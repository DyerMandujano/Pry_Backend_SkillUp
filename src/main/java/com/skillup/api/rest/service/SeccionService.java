package com.skillup.api.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.model.Curso;
import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.repository.SeccionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class SeccionService {
	
	
	@Autowired
    private SeccionRepository seccionRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Seccion> listarSeccionesPorCurso(int idCurso) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_ListarSeccionesporCurso", Seccion.class);
        query.registerStoredProcedureParameter("idCurso", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("idCurso", idCurso);
        return query.getResultList();
    }
    
    public Seccion obtenerSeccionPorId(int id) {
        return seccionRepository.findById(id).orElse(null);
    }
	
	public void insertarSeccion(Seccion seccion) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_InsertSeccion");

        query.registerStoredProcedureParameter("Id_Curso", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Nombre_Seccion", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Orden_Seccion", Integer.class, jakarta.persistence.ParameterMode.IN);

        query.setParameter("Id_Curso", seccion.getIdCurso());
        query.setParameter("Nombre_Seccion", seccion.getNombreSeccion());
        query.setParameter("Orden_Seccion", seccion.getOrdenSeccion());

        query.execute();
    }
	
	public void actualizarSeccion(Seccion seccion) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_UpdateSeccionPorID");

        query.registerStoredProcedureParameter("Id_Seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Id_Curso", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Nombre_Seccion", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Orden_Seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("Estado", Integer.class, jakarta.persistence.ParameterMode.IN);

        query.setParameter("Id_Seccion", seccion.getIdSeccion());
        query.setParameter("Id_Curso", seccion.getIdCurso());
        query.setParameter("Nombre_Seccion", seccion.getNombreSeccion());
        query.setParameter("Orden_Seccion", seccion.getOrdenSeccion());
        query.setParameter("Estado", seccion.getEstado());

        query.execute();
    }
	
	public void eliminarSeccion(int idSeccion) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_DeleteSeccionPorID");

        query.registerStoredProcedureParameter("Id_Seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("Id_Seccion", idSeccion);

        query.execute();
    }
	

}
