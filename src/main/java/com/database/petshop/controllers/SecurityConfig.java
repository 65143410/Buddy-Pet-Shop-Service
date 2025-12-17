package com.database.petshop.controllers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ปิด CSRF เพื่อให้ POST ได้
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated() // ทุกเส้นทางต้อง Login
            )
            .httpBasic(Customizer.withDefaults()); // ใช้ Basic Auth (User/Pass)
        
        return http.build();
    }
}
