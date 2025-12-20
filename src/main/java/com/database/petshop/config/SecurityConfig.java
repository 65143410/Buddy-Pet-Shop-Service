package com.database.petshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/product/**", "/api/category/**").permitAll()
                .requestMatchers("/api/product/add", "/api/product/update/**", "/api/product/delete/**").hasRole("ADMIN")
                .requestMatchers("/api/category/add", "/api/category/update/**", "/api/category/delete/**").hasRole("ADMIN")
                .requestMatchers("/api/category/report/**", "/api/orders/report/**").hasRole("ADMIN")
                .requestMatchers("/api/orders/create").hasRole("STAFF")
                .requestMatchers("/api/orders/{id}/accept", "/api/orders/{id}/complete", "/api/orders/{id}/cancel").hasRole("STAFF")
                .requestMatchers("/api/orders/unassigned", "/api/orders/staff/**").hasRole("STAFF")
                .requestMatchers("/api/orders/all", "/api/orders/{id}", "/api/orders/search").hasAnyRole("ADMIN", "STAFF")
                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
