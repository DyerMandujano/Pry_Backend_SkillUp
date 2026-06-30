package com.skillup.api.rest.config; 

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry; // 🔹 NUEVA IMPORTACIÓN
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Enlace de la ruta web con la carpeta física
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();
        System.out.println("📂 Carpeta pública mapeada en: " + uploadPath);
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }

    // 🔹 NUEVO: Habilitar permisos CORS específicos para la carpeta de recursos estáticos
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/uploads/**")
                .allowedOrigins("http://localhost:4200") // Permite que tu app de Angular acceda al contenido
                .allowedMethods("GET", "OPTIONS")
                .allowedHeaders("*");
    }
}