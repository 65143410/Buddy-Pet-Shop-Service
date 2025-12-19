package com.database.petshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.controllers.AuthController.LoginRequest;
import com.database.petshop.entity.CustomerEntity;
import com.database.petshop.service.CustomerService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private CustomerService customerService;

    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    CustomerEntity user = customerService.login(loginRequest.getEmail(), loginRequest.getPassword());
    
    if (user != null) {
        return ResponseEntity.ok(user);
    }
    return ResponseEntity.status(401).body("อีเมลหรือรหัสผ่านไม่ถูกต้อง");
    }
class LoginRequest {
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