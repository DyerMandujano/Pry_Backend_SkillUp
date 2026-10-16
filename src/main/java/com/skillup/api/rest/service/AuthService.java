package com.skillup.api.rest.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @PersistenceContext
    private EntityManager entityManager;

    // BCrypt: inyectado desde SecurityConfig via @Bean
    @Autowired
    private PasswordEncoder passwordEncoder;

    // (Mantenemos esto por si otros servicios los usan, aunque este no)
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PersonaRepository personaRepository;
    @Autowired private DocenteRepository docenteRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    

    /**
     * Valida el login usando EntityManager (JPQL).
     * Usa BCrypt para comparar contraseñas (seguro contra fuerza bruta).
     * Responde SIEMPRE "Credenciales incorrectas" tanto si el usuario no existe
     * como si la clave es incorrecta (proteccion contra enumeracion de usuarios).
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
            // SEGURIDAD: mismo mensaje que clave incorrecta (anti-enumeracion)
            throw new RuntimeException("Credenciales incorrectas");
        }

        String claveAlmacenada = usuario.getContrasenia();

        // AUTO-MIGRACION: si la clave es texto plano (no hash BCrypt), validamos
        // en texto plano y luego la convertimos a hash para el futuro.
        if (!claveAlmacenada.startsWith("$2a$") && !claveAlmacenada.startsWith("$2b$")) {
            // Clave en texto plano (usuario antiguo)
            if (!loginRequest.getContrasenia().equals(claveAlmacenada)) {
                throw new RuntimeException("Credenciales incorrectas");
            }
            // Migrar: actualizar la BD con el hash BCrypt
            String nuevoHash = passwordEncoder.encode(loginRequest.getContrasenia());
            usuario.setContrasenia(nuevoHash);
            usuarioRepository.save(usuario);
        } else {
            // Clave ya hasheada: usar BCrypt
            if (!passwordEncoder.matches(loginRequest.getContrasenia(), claveAlmacenada)) {
                throw new RuntimeException("Credenciales incorrectas");
            }
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
        
        if (request.getUsuario() == null || request.getUsuario().getUsername() == null) {
            throw new RuntimeException("Los datos de usuario son obligatorios");
        }

        Usuario usuario = request.getUsuario();

        // Validar robustez de contraseña segun politica OWASP
        validarRequisitosContrasenia(usuario.getContrasenia());

        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM Usuario u WHERE u.username = :username", 
            Long.class
        );
        query.setParameter("username", usuario.getUsername());
        
        if (query.getSingleResult() > 0) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        Persona persona = request.getPersona();
        if (persona == null) {
            throw new RuntimeException("Los datos de la persona son obligatorios");
        }

        // Validación de Nombres (solo letras)
        if (persona.getNombres() == null || persona.getNombres().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        persona.setNombres(persona.getNombres().trim());
        if (!persona.getNombres().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            throw new RuntimeException("El nombre solo debe contener letras");
        }

        // Validación de Apellidos (solo letras)
        if (persona.getApellidos() == null || persona.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
        persona.setApellidos(persona.getApellidos().trim());
        if (!persona.getApellidos().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            throw new RuntimeException("El apellido solo debe contener letras");
        }

        // Validación de Fecha de Nacimiento (mayor de 18 años)
        if (persona.getFechaDeNacimiento() == null) {
            throw new RuntimeException("La fecha de nacimiento es obligatoria");
        }
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = persona.getFechaDeNacimiento().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        if (Period.between(nacimiento, hoy).getYears() < 18) {
            throw new RuntimeException("Debes ser mayor de 18 años para registrarte");
        }

        // Si el DNI fue enviado en la raíz del request y no en persona
        if ((persona.getDni() == null || persona.getDni().trim().isEmpty()) && request.getDni() != null) {
            persona.setDni(request.getDni());
        }

        if (persona.getDni() == null || persona.getDni().trim().isEmpty()) {
            throw new RuntimeException("El DNI es obligatorio");
        }

        persona.setDni(persona.getDni().trim());

        if (persona.getDni().length() > 8) {
            throw new RuntimeException("El DNI no puede tener más de 8 caracteres");
        }

        TypedQuery<Long> queryDni = entityManager.createQuery(
            "SELECT COUNT(p) FROM Persona p WHERE p.dni = :dni", 
            Long.class
        );
        queryDni.setParameter("dni", persona.getDni());
        if (queryDni.getSingleResult() > 0) {
            throw new RuntimeException("El DNI ya se encuentra registrado");
        }
        
        try {
            // 1. Guardar la Persona (incluyendo el nuevo campo DNI)
            entityManager.persist(persona);
   
            int idPersonaGenerado = persona.getIdPersona();

            // 2. Preparar y guardar el Usuario con la contraseña hasheada en BCrypt
            usuario.setIdPersona(idPersonaGenerado);
            usuario.setFecRegistro(new java.util.Date());
            usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
            entityManager.persist(usuario);
            
            // 3. Insertar en el Rol Específico
            if (request.getDocente() != null) {
                Docente docente = request.getDocente();
                docente.setIdPersona(idPersonaGenerado);
                entityManager.persist(docente);
                
            } else if (request.getEstudiante() != null) {
                Estudiante estudiante = request.getEstudiante();
                estudiante.setIdPersona(idPersonaGenerado);
                entityManager.persist(estudiante);
            }
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    /**
     * Valida que la contraseña cumpla con los 5 requisitos de seguridad (OWASP):
     * 1. Minimo 8 caracteres
     * 2. Al menos una letra mayuscula
     * 3. Al menos una letra minuscula
     * 4. Al menos un numero
     * 5. Al menos un caracter especial
     */
    public void validarRequisitosContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < 8) {
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!contrasenia.matches(".*[A-Z].*")) {
            throw new RuntimeException("La contraseña debe incluir al menos una letra mayúscula");
        }
        if (!contrasenia.matches(".*[a-z].*")) {
            throw new RuntimeException("La contraseña debe incluir al menos una letra minúscula");
        }
        if (!contrasenia.matches(".*[0-9].*")) {
            throw new RuntimeException("La contraseña debe incluir al menos un número");
        }
        if (!contrasenia.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`].*")) {
            throw new RuntimeException("La contraseña debe incluir al menos un carácter especial");
        }
    }
}