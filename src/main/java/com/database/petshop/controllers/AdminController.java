package com.database.petshop.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all")
    public ResponseEntity<List<AdminEntity>> getAllAdmins() {
        return ResponseEntity.ok(adminService.findAllAdmins());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminEntity> updateAdmin(@PathVariable Long id, @RequestBody AdminEntity admin) {
        return ResponseEntity.ok(adminService.updateAdmin(id, admin));
    }

    @GetMapping("/pending-orders")
    public ResponseEntity<List<OrderEntity>> getPendingOrders() {

        return ResponseEntity.ok(adminService.findPendingOrders());
    }

    @PostMapping("/confirm-order/{orderId}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long orderId) {
        try {
            adminService.confirmPaymentAndDeductStock(orderId, 1L); 
            return ResponseEntity.ok("ยืนยันการชำระเงินและตัดสต็อกสินค้าสำเร็จ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<List<Map<String, Object>>> getStats() {
        
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

}
