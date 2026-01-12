package com.database.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.StaffEntity;
import com.database.petshop.service.StaffService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:4200")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/all")
    public ResponseEntity<List<StaffEntity>> getAllStaff() {
        return ResponseEntity.ok(staffService.findAllStaff());
    }

    @PostMapping("/add")
    public ResponseEntity<StaffEntity> createStaff(@Valid @RequestBody StaffEntity staff) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.saveStaff(staff));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStaffStatus(@PathVariable Long id, @Valid @RequestParam String newStatus) {
        staffService.updateStatus(id, newStatus);
        return ResponseEntity.ok("อัปเดตสถานะพนักงานเป็น " + newStatus + " เรียบร้อยแล้ว");
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffEntity> updateStaff(@PathVariable Long id, @RequestBody StaffEntity staff) {
        StaffEntity updatedStaff = staffService.updateStaff(id, staff);
        return ResponseEntity.ok(updatedStaff);
    }
}
