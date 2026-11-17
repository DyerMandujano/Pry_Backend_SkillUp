package com.skillup.api.rest.config;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de Seguridad HTTP y Capa de Transporte (OWASP ASVS V9 y V14)
 * Inyecta cabeceras defensivas para garantizar HTTPS estricto y mitigar ataques de degradación.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            // 1. Forzar HTTPS estricto (HSTS - HTTP Strict Transport Security)
            httpServletResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");

            // 2. Prevenir ataque de sniffing de tipo MIME (CWE-79 / CWE-116)
            httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");

            // 3. Mitigación de Clickjacking (CWE-1021)
            httpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");

            // 4. Política de referencia segura para no fugar rutas sensibles en encabezados Referer
            httpServletResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

            // 5. Permisos de características del navegador restringidas
            httpServletResponse.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
        }

        chain.doFilter(request, response);
    }
}
