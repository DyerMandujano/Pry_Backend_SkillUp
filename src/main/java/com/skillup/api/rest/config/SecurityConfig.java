package com.skillup.api.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuracion de seguridad de la aplicacion.
 * Registra el BCryptPasswordEncoder como Bean de Spring para poder
 * inyectarlo con @Autowired en cualquier servicio que necesite
 * hashear o verificar contraseñas.
 *
 * NOTA: Solo se usa spring-security-crypto (NO spring-boot-starter-security),
 * por lo tanto NO activa ningun filtro web de Spring Security.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Cost factor 10 = 2^10 = 1024 rondas de iteracion (estandar OWASP)
        return new BCryptPasswordEncoder();
    }
}
