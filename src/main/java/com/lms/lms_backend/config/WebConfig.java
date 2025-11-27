package com.lms.lms_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
