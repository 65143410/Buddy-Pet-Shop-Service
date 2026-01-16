package com.database.petshop.config;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.entity.CancelOrderEntity;
import com.database.petshop.entity.CategoryEntity;
import com.database.petshop.entity.CustomerEntity;
import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.PaymentEntity;
import com.database.petshop.entity.PetEntity;
import com.database.petshop.entity.ProductEntity;
import com.database.petshop.entity.StaffEntity;
import com.database.petshop.entity.StatusEntity;
import com.database.petshop.repository.AdminRepository;
import com.database.petshop.repository.CancelOrderRepository;
import com.database.petshop.repository.CategoryRepository;
import com.database.petshop.repository.CustomerRepository;
import com.database.petshop.repository.OrderDetailRepository;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.PaymentRepository;
import com.database.petshop.repository.PetRepository;
import com.database.petshop.repository.ProductRepository;
import com.database.petshop.repository.StaffRepository;
import com.database.petshop.repository.StatusRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private StatusRepository statusRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private OrderDetailRepository orderDetailRepo;
    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private PetRepository petRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CancelOrderRepository cancelOrderRepo;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        boolean isStatusInitialized = statusRepo.count() > 0;
        boolean isOrderInitialized = orderRepo.count() > 0;

        if (isStatusInitialized && isOrderInitialized) {
            System.out.println(">>> Database already initialized (Status & Orders exist). Skipping.");
            return;
        }

        System.out.println(">>> Initializing Data from mock_data.json...");

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // Support Java 8 Date/Time

        try (InputStream inputStream = getClass().getResourceAsStream("/mock_data.json")) {
            MockDataRoot data = mapper.readValue(inputStream, MockDataRoot.class);

            // 1. Statuses
            Map<Long, StatusEntity> statusMap = new HashMap<>();
            if (data.statuses != null) {
                if (statusRepo.count() == 0) {
                    for (StatusDTO dto : data.statuses) {
                        StatusEntity entity = new StatusEntity();
                        entity.setStatusName(dto.statusName);
                        statusRepo.save(entity);
                        statusMap.put(dto.id, entity);
                    }
                } else {
                    List<StatusEntity> existing = statusRepo.findAll();
                    for (StatusDTO dto : data.statuses) {
                        existing.stream()
                                .filter(e -> e.getStatusName().equals(dto.statusName))
                                .findFirst()
                                .ifPresent(e -> statusMap.put(dto.id, e));
                    }
                }
            }

            // 2. Categories
            Map<Long, CategoryEntity> categoryMap = new HashMap<>();
            if (data.categories != null) {
                if (categoryRepo.count() == 0) {
                    for (CategoryDTO dto : data.categories) {
                        CategoryEntity entity = new CategoryEntity();
                        entity.setCategoryName(dto.categoryName);
                        categoryRepo.save(entity);
                        categoryMap.put(dto.id, entity);
                    }
                } else {
                    List<CategoryEntity> existing = categoryRepo.findAll();
                    for (CategoryDTO dto : data.categories) {
                        existing.stream()
                                .filter(e -> e.getCategoryName().equals(dto.categoryName))
                                .findFirst()
                                .ifPresent(e -> categoryMap.put(dto.id, e));
                    }
                }
            }

            // 3. Admins
            Map<Long, AdminEntity> adminMap = new HashMap<>();
            if (data.admins != null) {
                if (adminRepo.count() == 0) {
                    for (AdminDTO dto : data.admins) {
                        AdminEntity entity = new AdminEntity();
                        entity.setName(dto.name);
                        entity.setEmail(dto.email);
                        entity.setPassword(passwordEncoder.encode(dto.password));
                        adminRepo.save(entity);
                        adminMap.put(dto.id, entity);
                    }
                } else {
                    List<AdminEntity> existing = adminRepo.findAll();
                    for (AdminDTO dto : data.admins) {
                        existing.stream().filter(e -> e.getEmail().equals(dto.email)).findFirst()
                                .ifPresent(e -> adminMap.put(dto.id, e));
                    }
                }
            }

            // 4. Staffs
            Map<Long, StaffEntity> staffMap = new HashMap<>();
            if (data.staffs != null) {
                if (staffRepo.count() == 0) {
                    for (StaffDTO dto : data.staffs) {
                        StaffEntity entity = new StaffEntity();
                        entity.setName(dto.name);
                        entity.setEmail(dto.email);
                        entity.setPassword(passwordEncoder.encode(dto.password));
                        entity.setPosition(dto.position);
                        entity.setStatus(dto.status);
                        staffRepo.save(entity);
                        staffMap.put(dto.id, entity);
                    }
                } else {
                    List<StaffEntity> existing = staffRepo.findAll();
                    for (StaffDTO dto : data.staffs) {
                        existing.stream().filter(e -> e.getEmail().equals(dto.email)).findFirst()
                                .ifPresent(e -> staffMap.put(dto.id, e));
                    }
                }
            }

            // 5. Customers
            Map<Long, CustomerEntity> customerMap = new HashMap<>();
            if (data.customers != null) {
                if (customerRepo.count() == 0) {
                    for (CustomerDTO dto : data.customers) {
                        CustomerEntity entity = new CustomerEntity();
                        entity.setCustomerName(dto.customerName);
                        entity.setEmail(dto.email);
                        entity.setPassword(passwordEncoder.encode(dto.password));
                        entity.setPhone(dto.phone);
                        entity.setAddress(dto.address);
                        customerRepo.save(entity);
                        customerMap.put(dto.id, entity);
                    }
                } else {
                    List<CustomerEntity> existing = customerRepo.findAll();
                    for (CustomerDTO dto : data.customers) {
                        existing.stream().filter(e -> e.getEmail().equals(dto.email)).findFirst()
                                .ifPresent(e -> customerMap.put(dto.id, e));
                    }
                }
            }

            // 6. Pets
            if (data.pets != null && petRepo.count() == 0) {
                for (PetDTO dto : data.pets) {
                    PetEntity entity = new PetEntity();
                    entity.setPetName(dto.petName);
                    entity.setPetType(dto.petType);
                    entity.setCongenitalDisease(dto.congenitalDisease);
                    if (dto.customerId != null && customerMap.containsKey(dto.customerId)) {
                        entity.setCustomer(customerMap.get(dto.customerId));
                    }
                    petRepo.save(entity);
                }
            }

            // 7. Products
            Map<Long, ProductEntity> productMap = new HashMap<>();
            if (data.products != null) {
                if (productRepo.count() == 0) {
                    for (ProductDTO dto : data.products) {
                        ProductEntity entity = new ProductEntity();
                        entity.setProductName(dto.productName);
                        entity.setPrice(dto.price);
                        entity.setStock(dto.stock);
                        entity.setDescription(dto.description);
                        entity.setTargetPetType(dto.targetPetType);
                        entity.setSuitableForDisease(dto.suitableForDisease);

                        if (dto.categoryId != null)
                            entity.setCategory(categoryMap.get(dto.categoryId));
                        if (dto.adminId != null)
                            entity.setAdmin(adminMap.get(dto.adminId));
                        else if (!adminMap.isEmpty())
                            entity.setAdmin(adminMap.values().iterator().next());

                        productRepo.save(entity);
                        productMap.put(dto.id, entity);
                    }
                } else {
                    List<ProductEntity> existing = productRepo.findAll();
                    for (ProductDTO dto : data.products) {
                        existing.stream().filter(e -> e.getProductName().equals(dto.productName)).findFirst()
                                .ifPresent(e -> productMap.put(dto.id, e));
                    }
                }
            }

            // 8. Orders
            if (data.orders != null && orderRepo.count() == 0) {
                for (OrderDTO dto : data.orders) {
                    OrderEntity entity = new OrderEntity();
                    entity.setInvoiceNo(dto.invoiceNo);
                    if (dto.orderDate != null)
                        entity.setOrderDate(LocalDate.parse(dto.orderDate));
                    entity.setTotalAmount(dto.totalAmount);

                    if (dto.customerId != null)
                        entity.setCustomer(customerMap.get(dto.customerId));
                    if (dto.statusId != null)
                        entity.setStatus(statusMap.get(dto.statusId));
                    if (dto.staffId != null)
                        entity.setStaff(staffMap.get(dto.staffId));

                    orderRepo.save(entity);

                    // Details
                    if (dto.details != null) {
                        for (OrderDetailDTO detailDto : dto.details) {
                            OrderDetailEntity detail = new OrderDetailEntity();
                            detail.setOrder(entity);
                            if (detailDto.productId != null)
                                detail.setProduct(productMap.get(detailDto.productId));
                            detail.setQuantity(detailDto.quantity);
                            detail.setUnitPrice(detailDto.unitPrice);
                            orderDetailRepo.save(detail);
                        }
                    }

                    // Payments
                    if (dto.payments != null) {
                        for (PaymentDTO paymentDto : dto.payments) {
                            PaymentEntity payment = new PaymentEntity();
                            payment.setOrder(entity);
                            payment.setAmount(paymentDto.amount);
                            payment.setMethod(paymentDto.method);
                            if (paymentDto.paymentDate != null) {
                                try {
                                    payment.setPaymentDate(LocalDateTime.parse(paymentDto.paymentDate));
                                } catch (Exception e) {
                                }
                            }
                            payment.setSlipImage(paymentDto.slipImage);
                            paymentRepo.save(payment);
                        }
                    }

                    // Cancellation
                    if (dto.cancellation != null) {
                        CancelOrderEntity cancel = new CancelOrderEntity();
                        cancel.setOrder(entity);
                        cancel.setReason(dto.cancellation.reason);
                        if (dto.cancellation.cancelDate != null)
                            cancel.setCancelDate(LocalDateTime.parse(dto.cancellation.cancelDate));
                        if (dto.cancellation.staffId != null)
                            cancel.setStaff(staffMap.get(dto.cancellation.staffId));
                        cancelOrderRepo.save(cancel);
                    }
                }

                System.out.println(">>> [SUCCESS] Orders have been restored/initialized.");
            } else {
                System.out.println(">>> Orders already exist. Skipping order initialization.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(">>> [ERROR] Failed to load data from mock_data.json: " + e.getMessage());
        }
    }

    // Helper to check if entity exists (simplified for this context)
    // In a real app, you might want more robust checks, but for mock data, IDs or
    // Names suffice.

    // --- DTO Classes for JSON Mapping ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MockDataRoot {
        public List<StatusDTO> statuses;
        public List<CategoryDTO> categories;
        public List<AdminDTO> admins;
        public List<StaffDTO> staffs;
        public List<CustomerDTO> customers;
        public List<PetDTO> pets;
        public List<ProductDTO> products;
        public List<OrderDTO> orders;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class StatusDTO {
        public Long id;
        public String statusName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CategoryDTO {
        public Long id;
        public String categoryName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AdminDTO {
        public Long id;
        public String name;
        public String email;
        public String password;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class StaffDTO {
        public Long id;
        public String name;
        public String email;
        public String password;
        public String position;
        public String status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CustomerDTO {
        public Long id;
        public String customerName;
        public String email;
        public String password;
        public String phone;
        public String address;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class PetDTO {
        public Long petId;
        public String petName;
        public String petType;
        public String congenitalDisease;
        public Long customerId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ProductDTO {
        public Long id;
        public String productName;
        public BigDecimal price;
        public Integer stock;
        public String description;
        public Long categoryId;
        public String targetPetType;
        public String suitableForDisease;
        public Long adminId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class OrderDTO {
        public Long id;
        public String invoiceNo;
        public String orderDate;
        public BigDecimal totalAmount;
        public Long customerId;
        public Long statusId;
        public Long staffId;
        public List<OrderDetailDTO> details;
        public List<PaymentDTO> payments;
        public CancellationDTO cancellation;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class OrderDetailDTO {
        public Long productId;
        public Integer quantity;
        public BigDecimal unitPrice;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class PaymentDTO {
        public BigDecimal amount;
        public String method;
        public String paymentDate;
        public String slipImage;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CancellationDTO {
        public String reason;
        public String cancelDate;
        public Long staffId;
    }
}
