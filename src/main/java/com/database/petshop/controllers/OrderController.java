package com.database.petshop.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.dto.OrderRequestDTO;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable Long id) {
        OrderEntity order = orderService.findOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        try {
            OrderEntity order = orderService.createOrder(request.getOrder(), request.getDetails());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/accept")
    public ResponseEntity<?> acceptOrder(
            @PathVariable Long orderId,
            @RequestParam Long staffId) {
        return ResponseEntity.ok(orderService.acceptOrder(orderId, staffId));
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<OrderEntity>> getUnassignedOrders() {
        List<OrderEntity> unassigned = orderService.findUnassignedOrders();
        return ResponseEntity.ok(unassigned);
    }

    @PutMapping("/{orderId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.completeOrder(orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @GetMapping("/staff/{staffId}/my-tasks")
    public ResponseEntity<List<OrderEntity>> getMyTasks(@PathVariable Long staffId) {
        List<OrderEntity> myTasks = orderService.findOrdersByStaff(staffId);
        return ResponseEntity.ok(myTasks);
    }

    @GetMapping("/report/daily")
    public ResponseEntity<Map<String, Object>> getDailyReport(
            @RequestParam(required = false) String date) {

        LocalDate reportDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();

        Map<String, Object> report = orderService.getDailySalesReport(reportDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/report/top-sellers")
    public ResponseEntity<List<Map<String, Object>>> getTopSellers() {
        return ResponseEntity.ok(orderService.getTopSellingProducts(5));
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderEntity>> searchByCustomer(@RequestParam String customerName) {
        return ResponseEntity.ok(orderService.searchOrdersByCustomer(customerName));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<Map<String, Object>> getOrderReceipt(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getReceipt(id));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<?> verifyOrder(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {

        Boolean isApproved = payload.get("isApproved");

        if (isApproved == null) {
            throw new RuntimeException("กรุณาระบุสถานะ isApproved (true/false)");
        }

        orderService.verifyPayment(id, isApproved);

        String message = isApproved ? "อนุมัติการชำระเงินเรียบร้อย" : "ปฏิเสธสลิปและคืนสต็อกแล้ว";
        return ResponseEntity.ok(Map.of("message", message));
    }
}
