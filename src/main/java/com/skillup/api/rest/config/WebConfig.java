package com.skillup.api.rest.config; 

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
}