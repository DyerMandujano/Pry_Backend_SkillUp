package com.skillup.api.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.model.Seccion;
import com.skillup.api.rest.repository.LeccionReporsitory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;


@Service
public class LeccionService {

	
	 @Autowired
	    private LeccionReporsitory leccionRepository;

	    @PersistenceContext
	    private EntityManager entityManager;
	    
	    public List<Leccion> listarLeccionesPorSeccion(int idSeccion) {
	        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_ListarLeccionesporSeccion", Leccion.class);
	        query.registerStoredProcedureParameter("id_seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.setParameter("id_seccion", idSeccion);
	        return query.getResultList();
	    }
	    
	    public Leccion obtenerLeccionPorId(int id) {
	        return leccionRepository.findById(id).orElse(null);
	    }
	    
	    
	    public void insertarLeccion(Leccion leccion) {
	        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_InsertLeccion");

	        query.registerStoredProcedureParameter("Id_Seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Nombre_Leccion", String.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Duracion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Material_1", String.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Material_2", String.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Orden_Leccion", Integer.class, jakarta.persistence.ParameterMode.IN);
	       
	        query.setParameter("Id_Seccion", leccion.getIdSeccion());
	        query.setParameter("Nombre_Leccion", leccion.getNombreLeccion());
	        query.setParameter("Duracion", leccion.getDuracion());
	        query.setParameter("Material_1", leccion.getMaterial1());
	        query.setParameter("Material_2", leccion.getMaterial2());
	        query.setParameter("Orden_Leccion", leccion.getOrdenLeccion());
	        
	        query.execute();
	    }
	    
	 // 🔹 Actualizar lección mediante SP
	    public void actualizarLeccion(Leccion leccion) {
	        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_UpdateLeccionPorID");

	        query.registerStoredProcedureParameter("Id_Leccion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Id_Seccion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Nombre_Leccion", String.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Duracion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Material_1", String.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Material_2", String.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Orden_Leccion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.registerStoredProcedureParameter("Estado", Integer.class, jakarta.persistence.ParameterMode.IN);

	        query.setParameter("Id_Leccion", leccion.getIdLeccion());
	        query.setParameter("Id_Seccion", leccion.getIdSeccion());
	        query.setParameter("Nombre_Leccion", leccion.getNombreLeccion());
	        query.setParameter("Duracion", leccion.getDuracion());
	        query.setParameter("Material_1", leccion.getMaterial1());
	        query.setParameter("Material_2", leccion.getMaterial2());
	        query.setParameter("Orden_Leccion", leccion.getOrdenLeccion());
	        query.setParameter("Estado", leccion.getEstado());

	        query.execute();
	    }
	    
	    public void eliminarLeccion(int idLeccion) {
	        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_DeleteLeccionPorID");

	        query.registerStoredProcedureParameter("Id_Leccion", Integer.class, jakarta.persistence.ParameterMode.IN);
	        query.setParameter("Id_Leccion", idLeccion);

	        query.execute();
	    }

	    
}
