package com.skillup.api.rest.service;

// 1. Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.skillup.api.rest.repository.UsuarioRepository;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.repository.DocenteRepository;
import com.skillup.api.rest.repository.EstudianteRepository;


import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Docente;


import com.skillup.api.rest.dtos.LoginRequest;
import com.skillup.api.rest.dtos.RegistroRequest;
import com.skillup.api.rest.dtos.LoginResponse;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service
public class AuthService {

    // --- ¡¡NUEVO!! ---
    @PersistenceContext
    private EntityManager entityManager;
    
    // (Mantenemos esto por si otros servicios los usan, aunque este no)
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PersonaRepository personaRepository;
    @Autowired private DocenteRepository docenteRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    

    /**
     * Valida el login usando EntityManager (JPQL)
     */
    public LoginResponse validarLogin(LoginRequest loginRequest) {
        
        Usuario usuario;
        try {
            
            TypedQuery<Usuario> query = entityManager.createQuery(
                "SELECT u FROM Usuario u WHERE u.username = :username", 
                Usuario.class
            );
            query.setParameter("username", loginRequest.getUsername());
            usuario = query.getSingleResult(); 

        } catch (NoResultException e) {
            
            throw new RuntimeException("Credenciales incorrectas");
        }
        
       
        if (!loginRequest.getContrasenia().equals(usuario.getContrasenia())) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        
      
        
       
        Persona persona = entityManager.find(Persona.class, usuario.getIdPersona());
        String nombreCompleto = "Usuario";
        
        if (persona != null) {
            nombreCompleto = persona.getNombres() + " " + persona.getApellidos();
        }

        Integer idRolEspecifico = null;
        try {
            if (usuario.getRol().equalsIgnoreCase("docente")) {
               
                idRolEspecifico = entityManager.createQuery(
                    "SELECT d.idDocente FROM Docente d WHERE d.idPersona = :idp", 
                    Integer.class
                ).setParameter("idp", usuario.getIdPersona()).getSingleResult();
                
            } else if (usuario.getRol().equalsIgnoreCase("estudiante")) {
                idRolEspecifico = entityManager.createQuery(
                    "SELECT e.idEstudiante FROM Estudiante e WHERE e.idPersona = :idp", 
                    Integer.class
                ).setParameter("idp", usuario.getIdPersona()).getSingleResult();
            }
        } catch (NoResultException e) {
            idRolEspecifico = null; 
        }

        // 5. Generamos el Token (JWT)
        String token = "TOKEN_FALSO_GENERADO_PARA_" + usuario.getUsername(); 


        return new LoginResponse(
            usuario.getUsername(),
            usuario.getRol(),
            nombreCompleto,
            token,
            usuario.getIdUsuario(),   
            usuario.getIdPersona(),   
            idRolEspecifico           
        );
    }



    @Transactional
    public void registrarUsuario(RegistroRequest request) {
        
    
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM Usuario u WHERE u.username = :username", 
            Long.class
        );
        query.setParameter("username", request.getUsuario().getUsername());
        
        if (query.getSingleResult() > 0) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        
        try {
            Persona persona = request.getPersona();
            Usuario usuario = request.getUsuario();

            
            entityManager.persist(persona);
  
            
            int idPersonaGenerado = persona.getIdPersona();

            // 3. Preparar y guardar el Usuario
            usuario.setIdPersona(idPersonaGenerado);
            entityManager.persist(usuario);
            
            // 4. Insertar en el Rol Específico
            if (request.getDocente() != null) {
                Docente docente = request.getDocente();
                docente.setIdPersona(idPersonaGenerado);
                entityManager.persist(docente);
                
            } else if (request.getEstudiante() != null) {
                Estudiante estudiante = request.getEstudiante();
                estudiante.setIdPersona(idPersonaGenerado);
                entityManager.persist(estudiante);
            }
            
           
            
        } catch (Exception e) {

            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }
}