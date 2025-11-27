package com.lms.lms_backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/courses/**").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/upload/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs/**").permitAll()
                
                // Current user endpoints - any authenticated user
                .requestMatchers(HttpMethod.GET, "/users/me").authenticated()
                .requestMatchers(HttpMethod.GET, "/users/me/progress").authenticated()
                .requestMatchers(HttpMethod.POST, "/users/me/enrollments/**").authenticated()
                
                // User management endpoints - ADMIN only
                .requestMatchers(HttpMethod.GET, "/users/{userId}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/{userId}/progress").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users/{userId}/enrollments/**").hasRole("ADMIN")
                 .requestMatchers("/admin/**").hasRole("ADMIN")

                   // Quiz endpoints - secure access
                .requestMatchers(HttpMethod.GET, "/quizzes/{userId}/history").authenticated() // Service layer will validate
                .requestMatchers(HttpMethod.GET, "/quizzes/{quizId}/questions").authenticated() // Service layer will validate
                 .requestMatchers(HttpMethod.GET, "/quizzes/lesson/{lessonId}").authenticated()
                 .requestMatchers(HttpMethod.POST, "/quizzes/{userId}/submit").authenticated() // Service layer will validate
                
                // Other user endpoints - authenticated
                .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                
                // Quiz endpoints - authenticated
                .requestMatchers("/quizzes/**").authenticated()
                
                // Achievement endpoints - authenticated
                .requestMatchers(HttpMethod.GET, "/achievements/me").authenticated()
                .requestMatchers(HttpMethod.GET, "/achievements/**").authenticated()
                
                // AI endpoints - authenticated
                .requestMatchers("/ai/**").authenticated()
                
                // Admin endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}