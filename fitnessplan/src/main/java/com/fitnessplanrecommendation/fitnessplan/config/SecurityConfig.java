package com.fitnessplanrecommendation.fitnessplan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/signup", "/api/login", "/api/home", "/api/fitness/recommend").permitAll() // Allow access to signup, login, and recommend
                .anyRequest().authenticated() // Protect other endpoints
            )
            .formLogin(form -> form
                .loginPage("/api/login") // Use custom login page
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}