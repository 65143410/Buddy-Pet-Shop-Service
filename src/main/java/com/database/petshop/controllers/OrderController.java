package com.database.petshop.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.dto.OrderItemDTO;
import com.database.petshop.dto.OrderRequestDTO;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.service.PetShopService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final PetShopService petShopService;

    public OrderController(PetShopService petShopService) {
        this.petShopService = petShopService;
    }

    @PostMapping 
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO request) {
        try {
            List<Long> productIds = request.getItems().stream()
                                           .map(OrderItemDTO::getProductId)
                                           .collect(Collectors.toList());
            List<Integer> quantities = request.getItems().stream()
                                             .map(OrderItemDTO::getQuantity)
                                             .collect(Collectors.toList());
            OrderEntity newOrder = petShopService.createNewOrder(
                request.getCustomerId(),
                productIds,
                quantities
            );
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
