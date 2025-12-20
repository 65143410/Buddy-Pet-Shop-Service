package com.database.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.OrderEntity;
import com.database.petshop.service.OrderService;

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
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDTO request) {
        try {
            OrderEntity order = orderService.createOrder(request.getOrder(), request.getDetails());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public static class OrderRequestDTO {
        private OrderEntity order;
        private List<com.database.petshop.entity.OrderDetailEntity> details;
        public OrderRequestDTO() {}
        public OrderEntity getOrder() {
            return order;
        }
        public void setOrder(OrderEntity order) { this.order = order; }
        public List<com.database.petshop.entity.OrderDetailEntity> getDetails() {
            return details;
        }
        public void setDetails(List<com.database.petshop.entity.OrderDetailEntity> details) { this.details = details; }
    }
}
