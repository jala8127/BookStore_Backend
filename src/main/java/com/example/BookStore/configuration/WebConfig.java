package com.example.BookStore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS configuration bean
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("*");
            }
        };
    }

    // Serve static images from: http://localhost:8080/images/<filename>
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads/images"); // path on filesystem
        String uploadPath = uploadDir.toFile().getAbsolutePath(); // absolute path

        registry.addResourceHandler("/images/**") // URL path
                .addResourceLocations("file:" + uploadPath + "/"); // real path
    }
}