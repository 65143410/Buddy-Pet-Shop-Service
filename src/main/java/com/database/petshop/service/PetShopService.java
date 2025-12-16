package com.database.petshop.service;

import com.database.petshop.entity.CustomerEntity;
import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.ProductEntity;
import com.database.petshop.repository.CustomerRepository;
import com.database.petshop.repository.OrderDetailRepository;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PetShopService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public PetShopService(OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional 
    public OrderEntity createNewOrder(Long customerId, List<Long> productIds, List<Integer> quantities) {

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        OrderEntity newOrder = new OrderEntity();
        newOrder.setCustomer(customer);
        newOrder.setOrderDate(LocalDate.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            if (product.getStock() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product); 

            OrderDetailEntity orderDetail = new OrderDetailEntity();
        
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setUnitPrice(product.getPrice()); 
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(quantity)));
            newOrder.getOrderDetails().add(orderDetail);
        }

        newOrder.setTotalAmount(totalAmount);
        
        return orderRepository.save(newOrder);
    }
}