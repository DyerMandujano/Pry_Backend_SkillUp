package com.skillup.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.model.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.util.Date;
import java.util.List;

@Service
public class UsuarioService {
    //TODO RELACIONADO CON PERSONA USUARIO
    @Autowired
    private PersonaRepository personaRepository;
    

    @PersistenceContext
    private EntityManager entityManager;
    
   

 public Persona obtenerPerfil(int idPersona) {
        Persona persona = personaRepository.spBuscarPersonaPorID(idPersona);
        if (persona == null) {
            throw new RuntimeException("No se encontró el perfil para el ID " + idPersona);
        }
        return persona;
    }

public void actualizarPerfil(int idPersona, Persona datosNuevos) {
        
        if (datosNuevos.getNombres() == null || datosNuevos.getNombres().isEmpty()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }
        
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("dbo.SP_UpdatePersonaPorID");

        query.registerStoredProcedureParameter("id", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("nombres", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("apellidos", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("fecha_nac", Date.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("genero", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("estado", Integer.class, jakarta.persistence.ParameterMode.IN);

        // 4. Asigna los valores
        query.setParameter("id", idPersona); 
        query.setParameter("nombres", datosNuevos.getNombres()); 
        query.setParameter("apellidos", datosNuevos.getApellidos()); 
        query.setParameter("fecha_nac", datosNuevos.getFechaDeNacimiento()); 
        query.setParameter("genero", datosNuevos.getGenero()); 
        query.setParameter("estado", datosNuevos.getEstado()); 

  
        query.execute();
    }
    
     public List<Persona> listarTodasLasPersonas() {
        
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(
            "dbo.SP_ListarPersonas", 
            Persona.class  
        );
        return query.getResultList();
    }
}
