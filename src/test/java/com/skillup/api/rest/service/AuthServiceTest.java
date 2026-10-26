package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.LoginRequest;
import com.skillup.api.rest.dtos.LoginResponse;
import com.skillup.api.rest.dtos.RecuperarPasswordDTO;
import com.skillup.api.rest.dtos.RegistroRequest;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private AuthService authService;

    private RegistroRequest requestValido;

    @BeforeEach
    void setUp() {
        Persona persona = new Persona();
        persona.setNombres("Carlos");
        persona.setApellidos("Mendoza");
        persona.setDni("76543210");
        persona.setGenero("Masculino");
        persona.setFechaDeNacimiento(java.sql.Date.valueOf("2000-01-15"));

        Usuario usuario = new Usuario();
        usuario.setUsername("76543210@skillup.pe");
        usuario.setContrasenia("123456");
        usuario.setRol("estudiante");

        Estudiante estudiante = new Estudiante();
        estudiante.setNivelEducativo("Superior");

        requestValido = new RegistroRequest();
        requestValido.setPersona(persona);
        requestValido.setUsuario(usuario);
        requestValido.setEstudiante(estudiante);

        org.mockito.Mockito.lenient().when(entityManager.createStoredProcedureQuery(anyString())).thenReturn(storedProcedureQuery);
        org.mockito.Mockito.lenient().when(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any())).thenReturn(storedProcedureQuery);
        org.mockito.Mockito.lenient().when(storedProcedureQuery.setParameter(anyString(), any())).thenReturn(storedProcedureQuery);
        org.mockito.Mockito.lenient().when(storedProcedureQuery.execute()).thenReturn(true);
        org.mockito.Mockito.lenient().when(storedProcedureQuery.getOutputParameterValue("resultado")).thenReturn(0);
        org.mockito.Mockito.lenient().when(storedProcedureQuery.getOutputParameterValue("mensaje")).thenReturn("Exito");
    }

    @Test
    @DisplayName("CP-01: Registro exitoso delegando a SP_RegistrarUsuario")
    void testRegistroExitoso() {
        authService.registrarUsuario(requestValido);

        verify(entityManager).createStoredProcedureQuery("dbo.SP_RegistrarUsuario");
        verify(storedProcedureQuery).execute();
    }

    @Test
    @DisplayName("CP-02 [Negativo]: Falla si la BD rechaza DNI inválido por constraint CK_Persona_Dni")
    void testRegistroDniInvalido() {
        when(storedProcedureQuery.getOutputParameterValue("resultado")).thenReturn(-1);
        when(storedProcedureQuery.getOutputParameterValue("mensaje"))
            .thenReturn("The INSERT statement conflicted with the CHECK constraint 'CK_Persona_Dni'.");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("DNI es obligatorio y debe contener exactamente 8 dígitos numéricos"));
    }

    @Test
    @DisplayName("CP-03 [Negativo]: Falla si la contraseña tiene menos de 6 dígitos en la BD")
    void testRegistroContraseniaExcedeLongitud() {
        when(storedProcedureQuery.getOutputParameterValue("resultado")).thenReturn(-1);
        when(storedProcedureQuery.getOutputParameterValue("mensaje"))
            .thenReturn("The INSERT statement conflicted with the CHECK constraint 'CK_Usuario_ContraseniaNoVacia'.");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("mínimo 8"));
    }

    @Test
    @DisplayName("CP-04 [Seguridad]: Bloquea auto-asignación de rol admin en registro público")
    void testRegistroPublicoBloqueaRolAdmin() {
        requestValido.getUsuario().setRol("admin");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("No está autorizado a registrar cuentas de administrador"));
    }

    @Test
    @DisplayName("CP-05 [Seguridad]: Bloquea creación de más administradores (solo existe 1 administrador)")
    void testRegistrarAdminBloqueado() {
        requestValido.getUsuario().setRol("admin");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarAdmin(requestValido, "admin");
        });

        assertTrue(ex.getMessage().contains("no está permitido registrar más administradores"));
    }

    @Test
    @DisplayName("CP-07 [HU-22 Negativo]: Recuperación falla si las contraseñas no coinciden")
    void testRecuperarContraseniaNoCoinciden() {
        RecuperarPasswordDTO dto = new RecuperarPasswordDTO("cmendoza", "123456", "654321");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.restablecerContrasenia(dto);
        });

        assertTrue(ex.getMessage().contains("Las contraseñas no coinciden"));
    }

    @Test
    @DisplayName("CP-08 [HU-22 Negativo]: Recuperación falla si la contraseña tiene menos de 8 caracteres")
    void testRecuperarContraseniaExcedeLongitud() {
        RecuperarPasswordDTO dto = new RecuperarPasswordDTO("cmendoza", "12345", "12345");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.restablecerContrasenia(dto);
        });

        assertTrue(ex.getMessage().contains("mínimo 8"));
    }

    @Test
    @DisplayName("CP-09 [Negativo]: Registro falla si la fecha de nacimiento es actual/futura o menor de 12 años")
    void testRegistroFechaNacimientoActualOFutura() {
        when(storedProcedureQuery.getOutputParameterValue("resultado")).thenReturn(-1);
        when(storedProcedureQuery.getOutputParameterValue("mensaje"))
            .thenReturn("The INSERT statement conflicted with the CHECK constraint 'CK_Persona_FechaNacimiento'.");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("La fecha de nacimiento no puede ser la fecha actual ni una fecha futura"));
    }

    @Test
    @DisplayName("CP-10 [Seguridad / Integridad]: Falla si el DNI ya existe en la base de datos (UQ_Persona_Dni)")
    void testRegistroDniDuplicado() {
        when(storedProcedureQuery.getOutputParameterValue("resultado")).thenReturn(-1);
        when(storedProcedureQuery.getOutputParameterValue("mensaje"))
            .thenReturn("Violation of UNIQUE KEY constraint 'UQ_Persona_Dni'. Cannot insert duplicate key.");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("El número de DNI ingresado ya se encuentra registrado"));
    }

    @Test
    @DisplayName("CP-11 [Integridad]: Falla si el nombre y apellido ya existen en la base de datos (UQ_Persona_NombreApellido)")
    void testRegistroNombreApellidoDuplicado() {
        when(storedProcedureQuery.getOutputParameterValue("resultado")).thenReturn(-1);
        when(storedProcedureQuery.getOutputParameterValue("mensaje"))
            .thenReturn("Violation of UNIQUE KEY constraint 'UQ_Persona_NombreApellido'. Cannot insert duplicate key.");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(requestValido);
        });

        assertTrue(ex.getMessage().contains("Ya existe una persona registrada con los mismos nombres y apellidos"));
    }

    @Test
    @DisplayName("CP-12 [Login]: Validación de login exitosa mediante SP_ValidarLogin con Hash SHA2_512")
    void testLoginExitoso() {
        when(storedProcedureQuery.getOutputParameterValue("es_valido")).thenReturn(true);
        when(storedProcedureQuery.getOutputParameterValue("id_usuario")).thenReturn(10);
        when(storedProcedureQuery.getOutputParameterValue("id_persona")).thenReturn(20);
        when(storedProcedureQuery.getOutputParameterValue("rol")).thenReturn("estudiante");
        when(storedProcedureQuery.getOutputParameterValue("nombre_completo")).thenReturn("Carlos Mendoza");
        when(storedProcedureQuery.getOutputParameterValue("usuario_retorno")).thenReturn("76543210@skillup.pe");
        when(storedProcedureQuery.getOutputParameterValue("id_rol_especifico")).thenReturn(5);

        LoginRequest loginReq = new LoginRequest("76543210@skillup.pe", "123456");
        LoginResponse response = authService.validarLogin(loginReq);

        assertNotNull(response);
        assertEquals("76543210@skillup.pe", response.getUsername());
        assertEquals("estudiante", response.getRol());
        assertEquals("Carlos Mendoza", response.getNombreCompleto());
        assertEquals(10, response.getIdUsuario());
        assertEquals(20, response.getIdPersona());
        assertEquals(5, response.getIdRolEspecifico());
    }

    @Test
    @DisplayName("CP-13 [Login Negativo]: Login falla si la base de datos rechaza credenciales")
    void testLoginCredencialesIncorrectas() {
        when(storedProcedureQuery.getOutputParameterValue("es_valido")).thenReturn(false);
        when(storedProcedureQuery.getOutputParameterValue("mensaje")).thenReturn("Credenciales incorrectas.");

        LoginRequest loginReq = new LoginRequest("76543210@skillup.pe", "clave_errada");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.validarLogin(loginReq);
        });

        assertEquals("Credenciales incorrectas.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-13b [Login Bloqueo]: Login falla si la cuenta fue inactivada temporalmente por 30 minutos")
    void testLoginCuentaInactivaPorBloqueo() {
        when(storedProcedureQuery.getOutputParameterValue("es_valido")).thenReturn(false);
        when(storedProcedureQuery.getOutputParameterValue("mensaje"))
            .thenReturn("Cuenta inactiva por seguridad durante 30 minutos debido a 3 intentos fallidos.");

        LoginRequest loginReq = new LoginRequest("76543210@skillup.pe", "clave_errada");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.validarLogin(loginReq);
        });

        assertTrue(ex.getMessage().contains("Cuenta inactiva por seguridad"));
        assertTrue(ex.getMessage().contains("30 minutos"));
    }

    @Test
    @DisplayName("CP-14 [SMS OTP]: Generación exitosa de código SMS OTP")
    void testGenerarCodigoSmsExitoso() {
        when(storedProcedureQuery.getOutputParameterValue("resultado")).thenReturn(0);
        when(storedProcedureQuery.getOutputParameterValue("mensaje")).thenReturn("Código SMS generado exitosamente.");
        when(storedProcedureQuery.getOutputParameterValue("codigo_generado")).thenReturn("123456");

        java.util.Map<String, Object> resp = authService.generarCodigoSms("987654321", "REGISTRO", null);

        assertNotNull(resp);
        assertEquals(0, resp.get("resultado"));
        assertEquals("123456", resp.get("codigoSmsSimulado"));
    }

    @Test
    @DisplayName("CP-15 [SMS OTP Negativo]: Rechaza celular que no cumple formato de 9 dígitos de Perú")
    void testGenerarCodigoSmsCelularInvalido() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.generarCodigoSms("12345", "REGISTRO", null);
        });

        assertTrue(ex.getMessage().contains("exactamente 9 dígitos"));
    }
}
