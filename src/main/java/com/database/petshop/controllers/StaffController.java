package com.database.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.StaffEntity;
import com.database.petshop.service.StaffService;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    @Autowired private StaffService staffService;

    @GetMapping("/all")
    public ResponseEntity<List<StaffEntity>> getAllStaff() {
        return ResponseEntity.ok(staffService.findAllStaff());
    }

    @PostMapping("/add")
    public ResponseEntity<StaffEntity> createStaff(@RequestBody StaffEntity staff) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.saveStaff(staff));
    }
}
