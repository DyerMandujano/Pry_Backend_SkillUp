package com.skillup.api.rest.controller;

// 1. Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * Controlador REST para manejar la autenticación (Login) y el Registro.
 * Esta es la "puerta de entrada" (endpoint) para Angular.
 * Reemplaza a LoginServlet.
 */
@RestController // (Spring) Marca esta clase como un Controlador REST (devuelve JSON)
@RequestMapping("/api/auth") // (Spring) URL base para todos los métodos: http://localhost:8080/api/auth
@CrossOrigin(origins = "http://localhost:4200") // (Spring) ¡IMPORTANTE! Permite llamadas desde Angular
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
            // 3. Si falla (ej: "usuario ya existe"): Devuelve 400 Bad Request
            // y el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
