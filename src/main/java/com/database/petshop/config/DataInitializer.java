package com.database.petshop.config;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.entity.CancelOrderEntity;
import com.database.petshop.entity.CategoryEntity;
import com.database.petshop.entity.CustomerEntity;
import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderDetailId;
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StatusRepository statusRepo;
    private final CategoryRepository categoryRepo;
    private final AdminRepository adminRepo;
    private final StaffRepository staffRepo;
    private final CustomerRepository customerRepo;
    private final PetRepository petRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;
    private final PaymentRepository paymentRepo;
    private final CancelOrderRepository cancelOrderRepo;
    private final PasswordEncoder passwordEncoder;

    
    private Map<Long, StatusEntity> statusMap = new HashMap<>();
    private Map<Long, CategoryEntity> categoryMap = new HashMap<>();
    private Map<Long, AdminEntity> adminMap = new HashMap<>();
    private Map<Long, StaffEntity> staffMap = new HashMap<>();
    private Map<Long, CustomerEntity> customerMap = new HashMap<>();
    private Map<Long, ProductEntity> productMap = new HashMap<>();
    private Map<Long, OrderEntity> orderMap = new HashMap<>();

    public DataInitializer(StatusRepository statusRepo, CategoryRepository categoryRepo, AdminRepository adminRepo,
            StaffRepository staffRepo, CustomerRepository customerRepo, PetRepository petRepo,
            ProductRepository productRepo, OrderRepository orderRepo, OrderDetailRepository orderDetailRepo,
            PaymentRepository paymentRepo, CancelOrderRepository cancelOrderRepo, PasswordEncoder passwordEncoder) {
        this.statusRepo = statusRepo;
        this.categoryRepo = categoryRepo;
        this.adminRepo = adminRepo;
        this.staffRepo = staffRepo;
        this.customerRepo = customerRepo;
        this.petRepo = petRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderDetailRepo = orderDetailRepo;
        this.paymentRepo = paymentRepo;
        this.cancelOrderRepo = cancelOrderRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (statusRepo.count() > 0 && orderRepo.count() > 0) {
            System.out.println(">>> Database already initialized. Skipping.");
            return;
        }

        System.out.println(">>> Initializing Data from JSON files...");
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); 

        
        loadStatus(mapper);

        
        loadCategory(mapper);

        
        loadAdmin(mapper);

        
        loadStaff(mapper);

        
        loadCustomer(mapper);

        
        loadPet(mapper);

        
        loadProduct(mapper);

        
        loadOrder(mapper);

        
        loadOrderDetail(mapper);

        
        loadPayment(mapper);

        
        loadCancelOrder(mapper);

        System.out.println("[SUCCESS] All Data Initialized Successfully.");
    }

    

    private void loadStatus(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/statuses.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, StatusJsonDTO.class);
            List<StatusJsonDTO> dtos = mapper.readValue(is, listType);
            for (StatusJsonDTO dto : dtos) {
                StatusEntity entity = new StatusEntity();
                entity.setStatusName(dto.statusName);
                
                
                StatusEntity saved = statusRepo.save(entity);
                statusMap.put(dto.id, saved);
            }
            System.out.println(">>> Loaded " + dtos.size() + " statuses.");
        }
    }

    private void loadCategory(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/categories.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class,
                    CategoryJsonDTO.class);
            List<CategoryJsonDTO> dtos = mapper.readValue(is, listType);
            for (CategoryJsonDTO dto : dtos) {
                CategoryEntity entity = new CategoryEntity();
                entity.setCategoryName(dto.categoryName);
                CategoryEntity saved = categoryRepo.save(entity);
                categoryMap.put(dto.id, saved);
            }
            System.out.println(">>> Loaded " + dtos.size() + " categories.");
        }
    }

    private void loadAdmin(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/admins.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, AdminJsonDTO.class);
            List<AdminJsonDTO> dtos = mapper.readValue(is, listType);
            for (AdminJsonDTO dto : dtos) {
                AdminEntity entity = new AdminEntity();
                entity.setName(dto.name);
                entity.setEmail(dto.email);
                entity.setPhone(dto.phone);
                entity.setPassword(passwordEncoder.encode(dto.password));
                AdminEntity saved = adminRepo.save(entity);
                adminMap.put(dto.id, saved);
            }
            System.out.println(">>> Loaded " + dtos.size() + " admins.");
        }
    }

    private void loadStaff(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/staffs.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, StaffJsonDTO.class);
            List<StaffJsonDTO> dtos = mapper.readValue(is, listType);
            for (StaffJsonDTO dto : dtos) {
                StaffEntity entity = new StaffEntity();
                entity.setName(dto.name);
                entity.setEmail(dto.email);
                entity.setPhone(dto.phone);
                entity.setPosition(dto.position);
                entity.setPassword(passwordEncoder.encode(dto.password));
                if (dto.status != null)
                    entity.setStatus(dto.status);
                StaffEntity saved = staffRepo.save(entity);
                staffMap.put(dto.id, saved);
            }
            System.out.println(">>> Loaded " + dtos.size() + " staffs.");
        }
    }

    private void loadCustomer(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/customers.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class,
                    CustomerJsonDTO.class);
            List<CustomerJsonDTO> dtos = mapper.readValue(is, listType);
            for (CustomerJsonDTO dto : dtos) {
                CustomerEntity entity = new CustomerEntity();
                entity.setCustomerName(dto.customerName);
                entity.setEmail(dto.email);
                entity.setPhone(dto.phone);
                entity.setAddress(dto.address);
                entity.setPassword(passwordEncoder.encode(dto.password));
                entity.setStatus(dto.status);
                CustomerEntity saved = customerRepo.save(entity);
                customerMap.put(dto.customerId, saved);
            }
            System.out.println(">>> Loaded " + dtos.size() + " customers.");
        }
    }

    private void loadPet(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/pets.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, PetJsonDTO.class);
            List<PetJsonDTO> dtos = mapper.readValue(is, listType);
            for (PetJsonDTO dto : dtos) {
                PetEntity entity = new PetEntity();
                entity.setPetName(dto.petName);
                entity.setPetType(dto.petType);
                entity.setCongenitalDisease(dto.congenitalDisease);
                if (dto.birthdate != null)
                    entity.setBirthdate(LocalDate.parse(dto.birthdate));
                entity.setWeight(dto.weight);
                entity.setGender(dto.gender);
                entity.setBreed(dto.breed);
                entity.setImage(dto.image);
                entity.setIsSterilized(dto.isSterilized);

                if (dto.customerId != null)
                    entity.setCustomer(customerMap.get(dto.customerId));

                petRepo.save(entity);
            }
            System.out.println(">>> Loaded " + dtos.size() + " pets.");
        }
    }

    private void loadProduct(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/products.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, ProductJsonDTO.class);
            List<ProductJsonDTO> dtos = mapper.readValue(is, listType);
            for (ProductJsonDTO dto : dtos) {
                ProductEntity entity = new ProductEntity();
                entity.setProductName(dto.productName);
                entity.setPrice(dto.price);
                entity.setStock(dto.stock);
                entity.setDescription(dto.description);
                entity.setImage(dto.image);
                entity.setTargetPetType(dto.targetPetType);
                entity.setSuitableForDisease(dto.suitableForDisease);
                entity.setIsActive(dto.isActive);
                entity.setBrand(dto.brand);
                entity.setWeightVolume(dto.weightVolume);

                if (dto.categoryId != null)
                    entity.setCategory(categoryMap.get(dto.categoryId));
                if (dto.adminId != null)
                    entity.setAdmin(adminMap.get(dto.adminId));

                ProductEntity saved = productRepo.save(entity);
                productMap.put(dto.productId, saved);
            }
            System.out.println(">>> Loaded " + dtos.size() + " products.");
        }
    }

    private void loadOrder(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/orders.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, OrderJsonDTO.class);
            List<OrderJsonDTO> dtos = mapper.readValue(is, listType);
            for (OrderJsonDTO dto : dtos) {
                OrderEntity entity = new OrderEntity();
                if (dto.orderDate != null)
                    entity.setOrderDate(LocalDate.parse(dto.orderDate));
                entity.setTotalAmount(dto.totalAmount);
                entity.setInvoiceNo(dto.invoiceNo);
                entity.setShippingAddress(dto.shippingAddress);
                entity.setTrackingNumber(dto.trackingNumber);
                entity.setShippingCost(dto.shippingCost);

                if (dto.customer != null)
                    entity.setCustomer(customerMap.get(dto.customer.customerId));
                if (dto.staff != null)
                    entity.setStaff(staffMap.get(dto.staff.id));
                if (dto.status != null)
                    entity.setStatus(statusMap.get(dto.status.id));

                OrderEntity saved = orderRepo.save(entity);
                orderMap.put(dto.orderId, saved);
            }
            System.out.println(">>> Loaded " + dtos.size() + " orders.");
        }
    }

    private void loadOrderDetail(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/OrderDetail.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class,
                    OrderDetailJsonDTO.class);
            List<OrderDetailJsonDTO> dtos = mapper.readValue(is, listType);
            for (OrderDetailJsonDTO dto : dtos) {
                OrderDetailEntity entity = new OrderDetailEntity();
                entity.setQuantity(dto.quantity);
                entity.setUnitPrice(dto.unitPrice);

                Long orderId = (dto.order != null) ? dto.order.orderId : null;
                Long productId = (dto.product != null) ? dto.product.productId : null;

                if (orderId != null && productId != null) {
                    OrderEntity order = orderMap.get(orderId);
                    ProductEntity product = productMap.get(productId);
                    if (order != null && product != null) {
                        entity.setOrder(order);
                        entity.setProduct(product);

                        
                        orderDetailRepo.save(entity);
                    }
                }
            }
            System.out.println(">>> Loaded " + dtos.size() + " order details.");
        }
    }

    private void loadPayment(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/Payment.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, PaymentJsonDTO.class);
            List<PaymentJsonDTO> dtos = mapper.readValue(is, listType);
            for (PaymentJsonDTO dto : dtos) {
                PaymentEntity entity = new PaymentEntity();
                
                if (dto.paymentDate != null)
                    entity.setPaymentDate(LocalDateTime.parse(dto.paymentDate));
                entity.setAmount(dto.amount);
                entity.setMethod(dto.method);
                entity.setSlipImage(dto.slipImage);

                if (dto.order != null) {
                    OrderEntity order = orderMap.get(dto.order.orderId);
                    if (order != null)
                        entity.setOrder(order);
                }
                paymentRepo.save(entity);
            }
            System.out.println(">>> Loaded " + dtos.size() + " payments.");
        }
    }

    private void loadCancelOrder(ObjectMapper mapper) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/CancelOrder.json")) {
            if (is == null)
                return;
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class,
                    CancelOrderJsonDTO.class);
            List<CancelOrderJsonDTO> dtos = mapper.readValue(is, listType);
            for (CancelOrderJsonDTO dto : dtos) {
                CancelOrderEntity entity = new CancelOrderEntity();
                if (dto.cancelDate != null)
                    entity.setCancelDate(LocalDateTime.parse(dto.cancelDate));
                entity.setReason(dto.reason);

                if (dto.order != null) {
                    OrderEntity order = orderMap.get(dto.order.orderId);
                    if (order != null)
                        entity.setOrder(order);
                }
                if (dto.staff != null) {
                    StaffEntity staff = staffMap.get(dto.staff.id);
                    if (staff != null)
                        entity.setStaff(staff);
                }
                cancelOrderRepo.save(entity);
            }
            System.out.println(">>> Loaded " + dtos.size() + " cancelled orders.");
        }
    }

    

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class StatusJsonDTO {
        public Long id;
        public String statusName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CategoryJsonDTO {
        public Long id;
        public String categoryName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AdminJsonDTO {
        public Long id;
        public String name;
        public String email;
        public String phone;
        public String password;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class StaffJsonDTO {
        public Long id;
        public String name;
        public String email;
        public String phone;
        public String position;
        public String password;
        public String status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CustomerJsonDTO {
        public Long customerId;
        public String customerName;
        public String email;
        public String phone;
        public String address;
        public String password;
        public String status;
        public String image;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class PetJsonDTO {
        public Long petId;
        public String petName;
        public String petType;
        public String congenitalDisease;
        public String birthdate;
        public Double weight;
        public String gender;
        public String breed;
        public String image;
        public Boolean isSterilized;
        public Long customerId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ProductJsonDTO {
        public Long productId;
        public String productName;
        public BigDecimal price;
        public Integer stock;
        public String description;
        public String image;
        public Long categoryId;
        public Long adminId;
        public String targetPetType;
        public String suitableForDisease;
        public Boolean isActive;
        public String brand;
        public String weightVolume;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class OrderJsonDTO {
        public Long orderId;
        public String orderDate;
        public BigDecimal totalAmount;
        public String invoiceNo;
        public String shippingAddress;
        public String trackingNumber;
        public BigDecimal shippingCost;

        public CustomerRefDTO customer;
        public StaffRefDTO staff;
        public StatusRefDTO status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CustomerRefDTO {
        public Long customerId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class StaffRefDTO {
        public Long id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class StatusRefDTO {
        public Long id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class OrderDetailJsonDTO {
        public OrderRefDTO order;
        public ProductRefDTO product;
        public Integer quantity;
        public BigDecimal unitPrice;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class OrderRefDTO {
        public Long orderId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ProductRefDTO {
        public Long productId; 
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class PaymentJsonDTO {
        public Long paymentId;
        public String paymentDate;
        public BigDecimal amount;
        public String method;
        public String slipImage;
        public OrderRefDTO order;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CancelOrderJsonDTO {
        public Long cancelId;
        public String cancelDate;
        public String reason;
        public OrderRefDTO order;
        public StaffRefDTO staff;
    }
}
