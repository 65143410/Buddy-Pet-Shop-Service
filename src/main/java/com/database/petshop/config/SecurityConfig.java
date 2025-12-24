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
        // http
        //         .cors(Customizer.withDefaults())
        //         .csrf(csrf -> csrf.disable())
        //         .authorizeHttpRequests(auth -> auth
        //         .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**").permitAll()
        //         .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/product/**", "/api/category/**").permitAll()
        //         .requestMatchers("/api/product/add", "/api/product/update/**", "/api/product/delete/**").hasRole("ADMIN")
        //         .requestMatchers("/api/category/add", "/api/category/update/**", "/api/category/delete/**").hasRole("ADMIN")
        //         .requestMatchers("/api/category/report/**", "/api/orders/report/**").hasRole("ADMIN")
        //         .requestMatchers("/api/orders/create").hasRole("STAFF")
        //         .requestMatchers("/api/orders/{id}/accept", "/api/orders/{id}/complete", "/api/orders/{id}/cancel").hasRole("STAFF")
        //         .requestMatchers("/api/orders/unassigned", "/api/orders/staff/**").hasRole("STAFF")
        //         .requestMatchers("/api/orders/all", "/api/orders/{id}", "/api/orders/search").hasAnyRole("ADMIN", "STAFF")
        //         .requestMatchers("/api/orders/{id}/verify").hasRole("ADMIN")
        //         .requestMatchers("/api/payments/upload-slip/**").hasRole("USER") 
        //         .requestMatchers("/uploads/**").permitAll()
        //         .anyRequest().authenticated()
        //         )
        //         .httpBasic(Customizer.withDefaults());

        http
            .cors(Customizer.withDefaults()) // สำคัญสำหรับ Angular เพื่อไม่ให้ติด CORS
            .csrf(csrf -> csrf.disable())    // ปิด CSRF เพื่อให้ส่ง POST (Base64/File) ได้โดยไม่โดนบล็อก
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()   // <--- อนุญาตทุก Request โดยไม่ต้องมีเงื่อนไข
        )
        // ถ้าต้องการ bypass จริงๆ สามารถเอาบรรทัด httpBasic ออกได้เลยครับ
        .httpBasic(httpBasic -> httpBasic.disable());        

        return http.build();
    }

}
