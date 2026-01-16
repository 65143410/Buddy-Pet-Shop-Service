package com.database.petshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.ProductEntity;
import com.database.petshop.repository.AdminRepository;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.ProductRepository;
import com.database.petshop.repository.StaffRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<AdminEntity> findAllAdmins() {
        return adminRepo.findAll();
    }

    public AdminEntity login(String email, String rawPassword) {
        AdminEntity admin = adminRepo.findByEmail(email);
        if (admin != null && passwordEncoder.matches(rawPassword, admin.getPassword())) {
            return admin;
        }
        return null;
    }

    public AdminEntity saveAdmin(AdminEntity admin) {
        return adminRepo.save(admin);
    }

    public List<OrderEntity> findPendingOrders() {
        return orderRepo.findByStatus_StatusId(1L);
    }

    @Transactional
    public void confirmPaymentAndDeductStock(Long orderId, Long staffId) {

        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบคำสั่งซื้อ"));

        if (order.getStatus().getStatusId() != 1) {
            throw new RuntimeException("คำสั่งซื้อนี้ถูกดำเนินการไปแล้ว");
        }

        for (OrderDetailEntity detail : order.getOrderDetails()) {
            ProductEntity product = detail.getProduct();

            if (product.getStock() < detail.getQuantity()) {
                throw new RuntimeException("สินค้า " + product.getProductName() + " มีจำนวนไม่พอ");
            }

            product.setStock(product.getStock() - detail.getQuantity());
            productRepo.save(product);
        }

        orderRepo.save(order);
    }

    public List<Map<String, Object>> getDashboardStats() {
        List<Map<String, Object>> stats = new ArrayList<>();

        long orderCount = orderRepo.count();

        Double totalRevenue = orderRepo.sumTotalAmount();

        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        long staffCount = staffRepo.count();

        stats.add(Map.<String, Object>of("label", "คำสั่งซื้อทั้งหมด", "value", orderCount, "icon",
                "pi pi-shopping-cart"));
        stats.add(Map.<String, Object>of("label", "รายได้รวม", "value", totalRevenue, "icon", "pi pi-money-bill"));
        stats.add(Map.<String, Object>of("label", "พนักงานทั้งหมด", "value", staffCount, "icon", "pi pi-users"));

        return stats;
    }

    @Transactional
    public ProductEntity saveProductWithFilters(ProductEntity product) {
        if (product.getTargetPetType() == null)
            product.setTargetPetType("ALL");
        if (product.getSuitableForDisease() == null)
            product.setSuitableForDisease("NONE");

        return productRepo.save(product);
    }
}
