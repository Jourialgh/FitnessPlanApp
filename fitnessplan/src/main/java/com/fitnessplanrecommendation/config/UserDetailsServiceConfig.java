package com.fitnessplanrecommendation.config;

import com.fitnessplanrecommendation.model.User;
    import com.fitnessplanrecommendation.service.UserService;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Configuration
public class UserDetailsServiceConfig {
    private final UserService userService;
    
    public UserDetailsServiceConfig(UserService userService) {
        this.userService = userService;
    }
    
@Bean
public UserDetailsService userDetailsService() {
    return username -> {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
             user.getPassword(),
             Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
            );
        };
    }
}