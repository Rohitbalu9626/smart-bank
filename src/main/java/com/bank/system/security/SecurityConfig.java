package com.bank.system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Enable CORS using the corsConfigurationSource bean defined below
                .cors(Customizer.withDefaults())

                // 2. Disable CSRF for REST APIs (since requests are token/basic auth based)
                .csrf(csrf -> csrf.disable())

                // 3. Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // ✅ Allow all preflight OPTIONS requests to bypass authentication filters
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        // ✅ Allow anyone to hit the bank endpoints
                        .requestMatchers("/api/bank/**").permitAll()
                        // Permit all other endpoints
                        .anyRequest().permitAll()
                )

                // 4. Basic Auth
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Allowed frontend origins
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "https://your-frontend.onrender.com" // Update this with your actual deployed frontend domain
        ));

        // ✅ Allowed HTTP Methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Allowed Request Headers
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));

        // ✅ Exposed Response Headers
        configuration.setExposedHeaders(List.of("Authorization"));

        // ✅ Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
