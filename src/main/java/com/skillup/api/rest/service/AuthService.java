package com.skillup.api.rest.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillup.api.rest.dtos.LoginRequest;
import com.skillup.api.rest.dtos.LoginResponse;
import com.skillup.api.rest.dtos.RegistroRequest;
import com.skillup.api.rest.dtos.RecuperarPasswordDTO;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;

@Service
public class AuthService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Valida el login delegando la verificación a la Base de Datos (SP_ValidarLogin)
     * La BD valida el hash criptográfico SHA2_512 con Salt y retorna los datos del usuario.
     */
    public LoginResponse validarLogin(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getContrasenia() == null) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        try {
            StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("dbo.SP_ValidarLogin");
            sp.registerStoredProcedureParameter("identificador", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("contrasenia", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("es_valido", Boolean.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("id_usuario", Integer.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("id_persona", Integer.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("rol", String.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("nombre_completo", String.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("usuario_retorno", String.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("id_rol_especifico", Integer.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.OUT);

            sp.setParameter("identificador", loginRequest.getUsername().trim());
            sp.setParameter("contrasenia", loginRequest.getContrasenia());
            sp.execute();

            Boolean esValido = (Boolean) sp.getOutputParameterValue("es_valido");
            String mensaje = (String) sp.getOutputParameterValue("mensaje");

            if (esValido == null || !esValido) {
                throw new RuntimeException(mensaje != null ? mensaje : "Credenciales incorrectas");
            }

            Integer idUsuario = (Integer) sp.getOutputParameterValue("id_usuario");
            Integer idPersona = (Integer) sp.getOutputParameterValue("id_persona");
            String rol = (String) sp.getOutputParameterValue("rol");
            String nombreCompleto = (String) sp.getOutputParameterValue("nombre_completo");
            String usuarioRetorno = (String) sp.getOutputParameterValue("usuario_retorno");
            Integer idRolEspecifico = (Integer) sp.getOutputParameterValue("id_rol_especifico");

            String token = "TOKEN_FALSO_GENERADO_PARA_" + usuarioRetorno;

            return new LoginResponse(
                usuarioRetorno,
                rol,
                nombreCompleto,
                token,
                idUsuario,
                idPersona,
                idRolEspecifico
            );
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error durante la autenticación: " + e.getMessage());
        }
    }

    /**
     * Registro público de estudiantes y docentes delegando a la BD (SP_RegistrarUsuario)
     */
    @Transactional
    public void registrarUsuario(RegistroRequest request) {
        validarEstructuraRegistro(request);

        String rol = request.getUsuario().getRol();
        if (rol == null || (!rol.equalsIgnoreCase("estudiante") && !rol.equalsIgnoreCase("docente"))) {
            throw new RuntimeException("El registro público solo permite roles 'estudiante' o 'docente'. No está autorizado a registrar cuentas de administrador.");
        }

        ejecutarSPRegistro(request, rol.toLowerCase());
    }

    /**
     * Registro de administradores deshabilitado: Solo existe un administrador principal.
     */
    @Transactional
    public void registrarAdmin(RegistroRequest request, String rolEjecutor) {
        throw new RuntimeException("Acceso restringido: no está permitido registrar más administradores en la plataforma. Solo existe una cuenta de administrador.");
    }

    private void validarEstructuraRegistro(RegistroRequest request) {
        if (request == null || request.getPersona() == null || request.getUsuario() == null) {
            throw new RuntimeException("Los datos de persona y usuario son obligatorios");
        }
    }

    /**
     * Ejecuta el Stored Procedure de registro autónomo en SQL Server
     */
    private void ejecutarSPRegistro(RegistroRequest request, String rol) {
        Persona persona = request.getPersona();
        Usuario usuario = request.getUsuario();

        String nivelEducativo = (request.getEstudiante() != null && request.getEstudiante().getNivelEducativo() != null)
                ? request.getEstudiante().getNivelEducativo() : "Superior";
        String especialidad = (request.getDocente() != null && request.getDocente().getEspecialidad() != null)
                ? request.getDocente().getEspecialidad() : "General";
        String gradoAcademico = (request.getDocente() != null && request.getDocente().getGradoAcademico() != null)
                ? request.getDocente().getGradoAcademico() : "Licenciado";

        try {
            StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("dbo.SP_RegistrarUsuario");
            sp.registerStoredProcedureParameter("nombres", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("apellidos", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("fecha_nac", java.util.Date.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("genero", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("dni", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("contrasenia", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("rol", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("nivel_educativo", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("especialidad", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("grado_academico", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("id_usuario", Integer.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("usuario_creado", String.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("resultado", Integer.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.OUT);

            sp.setParameter("nombres", persona.getNombres() != null ? persona.getNombres().trim() : null);
            sp.setParameter("apellidos", persona.getApellidos() != null ? persona.getApellidos().trim() : null);
            sp.setParameter("fecha_nac", persona.getFechaDeNacimiento());
            sp.setParameter("genero", persona.getGenero());
            sp.setParameter("dni", persona.getDni() != null ? persona.getDni().trim() : null);
            sp.setParameter("contrasenia", usuario.getContrasenia());
            sp.setParameter("rol", rol);
            sp.setParameter("nivel_educativo", nivelEducativo);
            sp.setParameter("especialidad", especialidad);
            sp.setParameter("grado_academico", gradoAcademico);

            sp.execute();

            Integer resultado = (Integer) sp.getOutputParameterValue("resultado");
            String mensaje = (String) sp.getOutputParameterValue("mensaje");

            if (resultado == null || resultado != 0) {
                throw new RuntimeException(mapearMensajeErrorBD(mensaje));
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(mapearMensajeErrorBD(e.getMessage()));
        }
    }

    /**
     * HU-22: Verifica si existe un usuario registrado por username o por DNI
     */
    public String verificarIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new RuntimeException("Ingrese un nombre de usuario o DNI válido.");
        }

        String idLimpio = identificador.trim();

        // Buscar por username
        TypedQuery<Usuario> queryUser = entityManager.createQuery(
            "SELECT u FROM Usuario u WHERE LOWER(u.username) = LOWER(:username)", Usuario.class
        );
        queryUser.setParameter("username", idLimpio);
        List<Usuario> usuarios = queryUser.getResultList();

        if (!usuarios.isEmpty()) {
            return usuarios.get(0).getUsername();
        }

        // Si no encontró por username, buscar por DNI en Persona
        TypedQuery<Persona> queryPersona = entityManager.createQuery(
            "SELECT p FROM Persona p WHERE p.dni = :dni", Persona.class
        );
        queryPersona.setParameter("dni", idLimpio);
        List<Persona> personas = queryPersona.getResultList();

        if (!personas.isEmpty()) {
            int idPersona = personas.get(0).getIdPersona();
            TypedQuery<Usuario> queryUserByPersona = entityManager.createQuery(
                "SELECT u FROM Usuario u WHERE u.idPersona = :idPersona", Usuario.class
            );
            queryUserByPersona.setParameter("idPersona", idPersona);
            List<Usuario> userList = queryUserByPersona.getResultList();
            if (!userList.isEmpty()) {
                return userList.get(0).getUsername();
            }
        }

        throw new RuntimeException("No se encontró ninguna cuenta asociada al usuario o DNI ingresado.");
    }

    /**
     * HU-22: Restablece la contraseña delegando el hasheado nativo y la actualización a SP_ActualizarPassword
     */
    @Transactional
    public void restablecerContrasenia(RecuperarPasswordDTO dto) {
        if (dto == null) {
            throw new RuntimeException("Los datos de restablecimiento son obligatorios.");
        }

        if (dto.getNuevaContrasenia() == null || dto.getNuevaContrasenia().trim().isEmpty()) {
            throw new RuntimeException("La nueva contraseña no puede estar vacía.");
        }

        if (dto.getNuevaContrasenia().length() < 6) {
            throw new RuntimeException("La contraseña debe tener como mínimo 6 dígitos o caracteres.");
        }

        if (!dto.getNuevaContrasenia().equals(dto.getConfirmarContrasenia())) {
            throw new RuntimeException("Las contraseñas no coinciden. Verifíquelas e intente nuevamente.");
        }

        try {
            StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("dbo.SP_ActualizarPassword");
            sp.registerStoredProcedureParameter("identificador", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("nueva_contrasenia", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("resultado", Integer.class, ParameterMode.OUT);
            sp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.OUT);

            sp.setParameter("identificador", dto.getIdentificador() != null ? dto.getIdentificador().trim() : "");
            sp.setParameter("nueva_contrasenia", dto.getNuevaContrasenia());
            sp.execute();

            Integer resultado = (Integer) sp.getOutputParameterValue("resultado");
            String mensaje = (String) sp.getOutputParameterValue("mensaje");

            if (resultado == null || resultado != 0) {
                throw new RuntimeException(mensaje != null ? mensaje : "Error al actualizar la contraseña.");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la contraseña: " + e.getMessage());
        }
    }

    /**
     * Mapea códigos de restricción de SQL Server a mensajes amigables para el usuario
     */
    private String mapearMensajeErrorBD(String error) {
        if (error == null) return "Error en la operación de base de datos.";
        if (error.contains("UQ_Persona_Dni")) {
            return "El número de DNI ingresado ya se encuentra registrado en el sistema.";
        }
        if (error.contains("UQ_Persona_NombreApellido")) {
            return "Ya existe una persona registrada con los mismos nombres y apellidos.";
        }
        if (error.contains("CK_Persona_FechaNacimiento")) {
            return "La fecha de nacimiento no puede ser la fecha actual ni una fecha futura, y debe tener al menos 12 años de edad.";
        }
        if (error.contains("CK_Persona_Dni")) {
            return "El DNI es obligatorio y debe contener exactamente 8 dígitos numéricos.";
        }
        if (error.contains("CK_Persona_NoVacio")) {
            return "Los nombres, apellidos y DNI son obligatorios y no pueden estar vacíos.";
        }
        if (error.contains("CK_Usuario_ContraseniaNoVacia") || error.contains("mínimo 6")) {
            return "La contraseña debe tener como mínimo 6 dígitos o caracteres.";
        }
        if (error.contains("UQ_Usuario_Usuario")) {
            return "El nombre de usuario institucional ya se encuentra registrado.";
        }
        return error;
    }
}