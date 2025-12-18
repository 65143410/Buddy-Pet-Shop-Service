package com.database.petshop.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.ProductEntity;
import com.database.petshop.repository.OrderDetailRepository;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.ProductRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderDetailRepository orderDetailRepo;

    @Autowired
    private ProductRepository productRepo;

    public List<OrderEntity> findAllOrders() {
        return orderRepo.findAll();
    }

    public OrderEntity findOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    @Transactional 
    public OrderEntity createOrder(OrderEntity order, List<OrderDetailEntity> details) {
        order.setOrderDate(LocalDate.now());
        OrderEntity savedOrder = orderRepo.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderDetailEntity detail : details) {
            ProductEntity product = productRepo.findById(detail.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("ไม่พบสินค้า ID: " + detail.getProduct().getProductId()));
            if (product.getStock() < detail.getQuantity()) {
                throw new RuntimeException("สินค้า " + product.getProductName() + " มีสต็อกไม่พอ");
            }

            product.setStock(product.getStock() - detail.getQuantity());
            productRepo.save(product);
            detail.setOrder(savedOrder);
            detail.setUnitPrice(product.getPrice());
            orderDetailRepo.save(detail);
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(detail.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        savedOrder.setTotalAmount(totalAmount);
        return orderRepo.save(savedOrder);
    }
}
