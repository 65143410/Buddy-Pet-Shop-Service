package com.database.petshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.CustomerEntity;
import com.database.petshop.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "ระบบจัดการการเข้าสู่ระบบ (Login)") // Swagger Group
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @Operation(
            summary = "ล็อกอินเข้าสู่ระบบ",
            description = "กรอก email และ password เพื่อตรวจสอบสิทธิ์การใช้งาน"
    )
    @ApiResponse(responseCode = "200", description = "เข้าสู่ระบบสำเร็จ",
            content = @Content(schema = @Schema(implementation = CustomerEntity.class)))
    @ApiResponse(responseCode = "401", description = "อีเมลหรือรหัสผ่านไม่ถูกต้อง")
    @PostMapping("/login") 
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        CustomerEntity user = customerService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("อีเมลหรือรหัสผ่านไม่ถูกต้อง");
    }

    
    public static class LoginRequest {

        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
