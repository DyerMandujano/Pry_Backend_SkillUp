package com.skillup.api.rest.controller;

// 1. Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 2. Importaciones de tus DTOs
import com.skillup.api.rest.dtos.LoginRequest;
import com.skillup.api.rest.dtos.LoginResponse;
import com.skillup.api.rest.dtos.RegistroRequest;
// 3. Importaciones de tu Servicio
import com.skillup.api.rest.service.AuthService;

/**
 Controlador REST para manejar la autenticación (Login) y el Registro.
  Esta es la "puerta de entrada" (endpoint) para Angular.
  Reemplaza a LoginServlet.
 */
@RestController // (Spring) Marca esta clase como un Controlador REST (devuelve JSON)
@RequestMapping("/api/auth") // (Spring) URL base para todos los métodos: http://localhost:8080/api/auth
public class AuthController {

    // 4. Inyecta el servicio (la cocina)
    @Autowired
    private AuthService authService;

    /**
     * Endpoint para el Login de usuario.
     * Escucha en: POST http://localhost:8080/api/auth/login
     * Reemplaza tu antiguo LoginServlet.doPost()
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        
        try {
            // 1. Llama a la cocina (servicio) para validar
            LoginResponse response = authService.validarLogin(loginRequest);
            

            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(e.getMessage());
        }
    }

    /**
     * Endpoint para el Registro de un nuevo usuario.
     * Escucha en: POST http://localhost:8080/api/auth/registrar
     * Reemplaza la lógica de registro de tu UsuarioDAO
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        
        try {
            // 1. Llama al servicio para ejecutar el registro transaccional
            authService.registrarUsuario(request);
            
            // 2. Si tiene éxito: Devuelve 200 OK con un mensaje
            return ResponseEntity.ok().body("Usuario registrado exitosamente");
            
        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint exclusivo para que un ADMIN pueda crear otro usuario ADMIN.
     * Escucha en: POST http://localhost:8080/api/auth/registrar-admin
     */
    @PostMapping("/registrar-admin")
    public ResponseEntity<?> registrarAdmin(
            @RequestBody RegistroRequest request,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String rolHeader,
            @org.springframework.web.bind.annotation.RequestParam(value = "rolEjecutor", required = false) String rolParam) {
        
        try {
            // El rol ejecutor se puede enviar por Header HTTP o por query param
            String rolEjecutor = (rolHeader != null && !rolHeader.trim().isEmpty()) ? rolHeader : rolParam;
            
            authService.registrarAdmin(request, rolEjecutor);
            return ResponseEntity.ok().body("Administrador registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * HU-22: Verifica si un identificador (username o DNI) existe para recuperar contraseña.
     * Escucha en: POST http://localhost:8080/api/auth/recuperar-password/verificar
     */
    @PostMapping("/recuperar-password/verificar")
    public ResponseEntity<?> verificarIdentificador(@RequestBody java.util.Map<String, String> body) {
        try {
            String identificador = body != null ? body.get("identificador") : null;
            java.util.Map<String, Object> detalle = authService.verificarIdentificadorDetallado(identificador);
            detalle.put("mensaje", "Usuario verificado exitosamente");
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            java.util.Map<String, String> err = new java.util.HashMap<>();
            err.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    /**
     * HU-22: Restablece la contraseña del usuario con nueva clave robusta y código SMS opcional.
     * Escucha en: POST http://localhost:8080/api/auth/recuperar-password/cambiar
     */
    @PostMapping("/recuperar-password/cambiar")
    public ResponseEntity<?> restablecerPassword(@RequestBody com.skillup.api.rest.dtos.RecuperarPasswordDTO dto) {
        try {
            authService.restablecerContrasenia(dto);
            java.util.Map<String, String> resp = new java.util.HashMap<>();
            resp.put("mensaje", "Contraseña actualizada exitosamente. Ahora puede iniciar sesión con su nueva clave.");
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            java.util.Map<String, String> err = new java.util.HashMap<>();
            err.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    /**
     * Endpoint para generar un código OTP vía SMS (válido por 5 minutos).
     * Escucha en: POST http://localhost:8080/api/auth/sms/generar
     */
    @PostMapping("/sms/generar")
    public ResponseEntity<?> generarCodigoSms(@RequestBody java.util.Map<String, Object> body) {
        try {
            String celular = body != null && body.get("celular") != null ? body.get("celular").toString() : null;
            String tipoOperacion = body != null && body.get("tipoOperacion") != null ? body.get("tipoOperacion").toString() : "REGISTRO";
            String identificador = body != null && body.get("identificador") != null ? body.get("identificador").toString() : null;
            Integer idPersona = null;

            // Si se proporciona un identificador (en recuperación), obtener el celular y el idPersona de la cuenta
            if ((celular == null || celular.trim().isEmpty()) && identificador != null && !identificador.trim().isEmpty()) {
                java.util.Map<String, Object> detalle = authService.verificarIdentificadorDetallado(identificador);
                celular = (String) detalle.get("celular");
                idPersona = (Integer) detalle.get("idPersona");
                if (celular == null) {
                    return ResponseEntity.badRequest().body(java.util.Map.of("mensaje", "La cuenta no tiene un número de celular registrado."));
                }
            }

            java.util.Map<String, Object> resp = authService.generarCodigoSms(celular, tipoOperacion, idPersona);
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("mensaje", e.getMessage()));
        }
    }

    /**
     * Endpoint para validar el código OTP ingresado por el usuario.
     * Escucha en: POST http://localhost:8080/api/auth/sms/validar
     */
    @PostMapping("/sms/validar")
    public ResponseEntity<?> validarCodigoSms(@RequestBody java.util.Map<String, String> body) {
        try {
            String celular = body != null ? body.get("celular") : null;
            String codigoIngresado = body != null ? body.get("codigoIngresado") : null;
            String tipoOperacion = body != null ? body.get("tipoOperacion") : "REGISTRO";

            java.util.Map<String, Object> resp = authService.validarCodigoSms(celular, codigoIngresado, tipoOperacion);
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("mensaje", e.getMessage()));
        }
    }
}
