package com.fitnessplanrecommendation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors().and()
            .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
            .requestMatchers("/api/users/all", "/api/users/update-role", "/api/users/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .httpBasic();
        return http.build();
    }


@Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}
}