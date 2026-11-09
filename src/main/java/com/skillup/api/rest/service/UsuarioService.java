package com.skillup.api.rest.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.repository.PersonaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class UsuarioService {
    //TODO RELACIONADO CON PERSONA USUARIO
    @Autowired
    private PersonaRepository personaRepository;
    

    @PersistenceContext
    private EntityManager entityManager;
    
   
    /**
     * CORRECCIÓN: Implementación manual con EntityManager para evitar fallos del SP.
     */
    public Persona obtenerPerfil(int idPersona) {
        // 1. Crear la llamada al SP (Asumiendo que el SP se llama dbo.SP_BuscarPersonaPorID)
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("dbo.SP_BuscarPersonaPorID", Persona.class);
        
        // 2. Registrar y pasar el parámetro
        query.registerStoredProcedureParameter("id", Integer.class, jakarta.persistence.ParameterMode.IN); 
        query.setParameter("id", idPersona);

        try {
            // 3. Ejecutar y obtener el resultado (que viene como una lista)
            @SuppressWarnings("unchecked")
            List<Persona> results = query.getResultList();
            
            if (results.isEmpty()) {
                throw new RuntimeException("No se encontró el perfil para el ID " + idPersona);
            }
            // 4. Devolver el primer resultado de la lista
            return results.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar perfil. Verifique la conexión a la DB. Mensaje: " + e.getMessage());
        }
    }

    public void actualizarPerfil(int idPersona, Persona datosNuevos) {
        
        if (datosNuevos.getNombres() == null || datosNuevos.getNombres().trim().isEmpty()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }

        if (datosNuevos.getApellidos() == null || datosNuevos.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos son obligatorios y no pueden estar vacíos");
        }

        if (datosNuevos.getFechaDeNacimiento() != null) {
            LocalDate fechaNac = new java.sql.Date(datosNuevos.getFechaDeNacimiento().getTime()).toLocalDate();
            LocalDate hoy = LocalDate.now();
            if (fechaNac.isAfter(hoy) || Period.between(fechaNac, hoy).getYears() < 12) {
                throw new RuntimeException("La fecha de nacimiento no puede ser la fecha actual ni una fecha futura, y debe tener al menos 12 años de edad.");
            }
        }

        String dni = (datosNuevos.getDni() != null && !datosNuevos.getDni().trim().isEmpty())
                ? datosNuevos.getDni().trim()
                : null;

        if (dni == null) {
            Persona actual = personaRepository.findById(idPersona).orElse(null);
            if (actual != null && actual.getDni() != null) {
                dni = actual.getDni().trim();
            }
        }

        if (dni == null || dni.isEmpty()) {
            throw new RuntimeException("El DNI es obligatorio y no puede estar vacío");
        }
        
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("dbo.SP_UpdatePersonaPorID");

        query.registerStoredProcedureParameter("id", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("nombres", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("apellidos", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("fecha_nac", Date.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("genero", String.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("estado", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.registerStoredProcedureParameter("dni", String.class, jakarta.persistence.ParameterMode.IN);

        int estadoFinal = datosNuevos.getEstado() > 0 ? datosNuevos.getEstado() : 1;

        // 4. Asigna los valores
        query.setParameter("id", idPersona); 
        query.setParameter("nombres", datosNuevos.getNombres().trim()); 
        query.setParameter("apellidos", datosNuevos.getApellidos().trim()); 
        query.setParameter("fecha_nac", datosNuevos.getFechaDeNacimiento()); 
        query.setParameter("genero", datosNuevos.getGenero()); 
        query.setParameter("estado", estadoFinal); 
        query.setParameter("dni", dni); 

        try {
            query.execute();
        } catch (Exception e) {
            String err = e.getMessage() != null ? e.getMessage() : "";
            Throwable t = e.getCause();
            while (t != null) {
                err += " " + t.getMessage();
                t = t.getCause();
            }
            if (err.contains("CK_Persona_FechaNacimiento")) {
                throw new RuntimeException("La fecha de nacimiento no puede ser la fecha actual ni una fecha futura, y debe tener al menos 12 años de edad.");
            }
            if (err.contains("CK_Persona_NoVacio")) {
                throw new RuntimeException("Los nombres y apellidos son obligatorios y no pueden estar vacíos.");
            }
            throw new RuntimeException("Error al actualizar perfil: " + e.getMessage());
        }
    }
    
    public List<Persona> listarTodasLasPersonas() {
        
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(
            "dbo.SP_ListarPersonas", 
            Persona.class  
        );
        return query.getResultList();
    }
}
