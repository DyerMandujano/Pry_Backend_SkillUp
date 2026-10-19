package com.skillup.api.rest.config; 

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration; // 🔹 NUEVA IMPORTACIÓN
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Enlace de la ruta web con la carpeta física
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();
        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }
        System.out.println("📂 Carpeta pública mapeada en: " + uploadPath);
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }

    // 🔹 NUEVO: Habilitar permisos CORS específicos para la carpeta de recursos estáticos
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/uploads/**")
                .allowedOrigins("http://192.168.56.1:4200", "http://localhost:4200")
                .allowedMethods("GET", "OPTIONS")
                .allowedHeaders("*");
    }
}