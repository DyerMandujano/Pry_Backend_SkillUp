package com.skillup.api.rest.service;

// 1. Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillup.api.rest.dtos.LoginRequest;
import com.skillup.api.rest.dtos.LoginResponse;
import com.skillup.api.rest.dtos.RegistroRequest;
import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.repository.DocenteRepository;
import com.skillup.api.rest.repository.EstudianteRepository;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.repository.UsuarioRepository;

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
        validarDatosRegistro(request);

        String rol = request.getUsuario().getRol();
        if (rol == null || (!rol.equalsIgnoreCase("estudiante") && !rol.equalsIgnoreCase("docente"))) {
            throw new RuntimeException("El registro público solo permite roles 'estudiante' o 'docente'. No está autorizado a registrar cuentas de administrador.");
        }

        guardarUsuarioYPersona(request);
    }

    @Transactional
    public void registrarAdmin(RegistroRequest request, String rolEjecutor) {
        if (rolEjecutor == null || !rolEjecutor.equalsIgnoreCase("admin")) {
            throw new RuntimeException("Acceso denegado: solo un usuario con rol ADMIN puede crear otro usuario ADMIN.");
        }

        validarDatosRegistro(request);
        request.getUsuario().setRol("admin");

        guardarUsuarioYPersona(request);
    }

    private void validarDatosRegistro(RegistroRequest request) {
        if (request.getPersona() == null || request.getUsuario() == null) {
            throw new RuntimeException("Los datos de persona y usuario son obligatorios");
        }

        Persona persona = request.getPersona();
        Usuario usuario = request.getUsuario();

        if (persona.getNombres() == null || persona.getNombres().trim().isEmpty()) {
            throw new RuntimeException("Los nombres son obligatorios");
        }
        if (persona.getApellidos() == null || persona.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos son obligatorios");
        }

        // Validación de DNI: obligatorio y exactamente 8 dígitos numéricos
        if (persona.getDni() == null || !persona.getDni().trim().matches("^\\d{8}$")) {
            throw new RuntimeException("El DNI es obligatorio y debe contener exactamente 8 dígitos numéricos");
        }

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }

        // Validación de Contraseña: obligatorio y máximo 6 caracteres/dígitos
        if (usuario.getContrasenia() == null || usuario.getContrasenia().trim().isEmpty()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        if (usuario.getContrasenia().length() > 6) {
            throw new RuntimeException("La contraseña debe tener como máximo 6 dígitos o caracteres");
        }

        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM Usuario u WHERE u.username = :username", 
            Long.class
        );
        query.setParameter("username", usuario.getUsername().trim());
        
        if (query.getSingleResult() > 0) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
    }

    private void guardarUsuarioYPersona(RegistroRequest request) {
        try {
            Persona persona = request.getPersona();
            Usuario usuario = request.getUsuario();

            if (persona.getDni() != null) {
                persona.setDni(persona.getDni().trim());
            }
            persona.setNombres(persona.getNombres().trim());
            persona.setApellidos(persona.getApellidos().trim());
            persona.setEstado(1); // Activo por defecto

            entityManager.persist(persona);
            int idPersonaGenerado = persona.getIdPersona();

            usuario.setIdPersona(idPersonaGenerado);
            usuario.setUsername(usuario.getUsername().trim());
            usuario.setFecRegistro(new java.util.Date());

            entityManager.persist(usuario);

            if (usuario.getRol().equalsIgnoreCase("docente") && request.getDocente() != null) {
                Docente docente = request.getDocente();
                docente.setIdPersona(idPersonaGenerado);
                docente.setEstado(1);
                entityManager.persist(docente);
            } else if (usuario.getRol().equalsIgnoreCase("estudiante") && request.getEstudiante() != null) {
                Estudiante estudiante = request.getEstudiante();
                estudiante.setIdPersona(idPersonaGenerado);
                estudiante.setEstado(1);
                entityManager.persist(estudiante);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }
}