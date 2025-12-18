package com.database.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
     @Autowired private AdminService adminService;

    @GetMapping("/all")
    public ResponseEntity<List<AdminEntity>> getAllAdmins() {
        return ResponseEntity.ok(adminService.findAllAdmins());
    }
   
}
