package com.skillup.api.rest.service;

// 1. Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 2. Importaciones de tus Repositories
import com.skillup.api.rest.repository.UsuarioRepository;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.repository.DocenteRepository;
import com.skillup.api.rest.repository.EstudianteRepository;

// 3. Importaciones de tus Entidades (Model)
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Docente;

// 4. Importaciones de tus DTOs
import com.skillup.api.rest.dtos.LoginRequest;
import com.skillup.api.rest.dtos.RegistroRequest;
import com.skillup.api.rest.dtos.LoginResponse;

/**
 * Capa de Servicio (Lógica de Negocio) para Autenticación.
 * Reemplaza la lógica de LoginServlet y UsuarioDAO.
 */
@Service // (Spring) Marca esta clase como un "Servicio"
public class AuthService {

    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PersonaRepository personaRepository;
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
public LoginResponse validarLogin(LoginRequest loginRequest) {
    
    // 1. Buscamos al usuario por username
    Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername());
    
    if (usuario == null) {
        throw new RuntimeException("Credenciales incorrectas");
    }
    
    // 2. Verificamos la contraseña (versión temporal)
    if (!loginRequest.getContrasenia().equals(usuario.getContrasenia())) {
        throw new RuntimeException("Credenciales incorrectas");
    }
    
    
    // 3. Buscamos la persona para obtener su nombre
    Persona persona = personaRepository.findById(usuario.getIdPersona()).orElse(null);
    String nombreCompleto = "Usuario";
    
    if (persona != null) {
        nombreCompleto = persona.getNombres() + " " + persona.getApellidos();
    }

    // --- 4. LÓGICA NUEVA: Buscar el ID de Rol (Docente o Estudiante) ---
    Integer idRolEspecifico = null;
    if (usuario.getRol().equalsIgnoreCase("docente")) {
        // Usamos el método que creamos en DocenteRepository
        idRolEspecifico = docenteRepository.findIdDocenteByIdPersona(usuario.getIdPersona());
        
    } else if (usuario.getRol().equalsIgnoreCase("estudiante")) {
        
    }

    // 5. Generamos el Token (JWT)
    String token = "TOKEN_FALSO_GENERADO_PARA_" + usuario.getUsername(); 

    // 6. Creamos y devolvemos el DTO de respuesta ¡COMPLETO!
    //    (Este es el constructor de 7 argumentos)
    return new LoginResponse(
        usuario.getUsername(),
        usuario.getRol(),
        nombreCompleto,
        token,
        usuario.getIdUsuario(),   // <-- ID de Usuario
        usuario.getIdPersona(),   // <-- ID de Persona
        idRolEspecifico           // <-- ID de Docente/Estudiante
    );
}


    @Transactional
    public void registrarUsuario(RegistroRequest request) {
        
        // 1. Validar si el email (username) ya existe
        if (usuarioRepository.existsByUsername(request.getUsuario().getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        
        try {
            Persona personaGuardada = personaRepository.save(request.getPersona());
            
            int idPersonaGenerado = personaGuardada.getIdPersona();

            // 3. Preparar y guardar el Usuario
            Usuario usuario = request.getUsuario();
            usuario.setIdPersona(idPersonaGenerado);

            usuarioRepository.save(usuario);
            
            // 4. Insertar en el Rol Específico
            if (request.getDocente() != null) {
                Docente docente = request.getDocente();
                docente.setIdPersona(idPersonaGenerado);
                docenteRepository.save(docente);
                
            } else if (request.getEstudiante() != null) {
                Estudiante estudiante = request.getEstudiante();
                estudiante.setIdPersona(idPersonaGenerado);
                estudianteRepository.save(estudiante);
            }
            
            // Si todo sale bien, @Transactional hará "commit" automáticamente
            
        } catch (Exception e) {
            // Si algo falla, @Transactional hará "rollback" automáticamente
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }
}