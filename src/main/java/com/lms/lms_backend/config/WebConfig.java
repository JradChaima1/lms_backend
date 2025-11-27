package com.lms.lms_backend.config;

import java.io.File;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:4200",
                    "https://*.vercel.app",
                    "https://angular-lms.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String currentPath = System.getProperty("user.dir");
        File uploadDir = new File(currentPath, "uploads");
        String uploadPath = "file:///" + uploadDir.getAbsolutePath().replace("\\", "/") + "/";
        System.out.println("Upload path configured: " + uploadPath);
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}
