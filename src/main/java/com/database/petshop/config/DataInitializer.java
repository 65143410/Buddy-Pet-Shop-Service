package com.database.petshop.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.entity.CategoryEntity;
import com.database.petshop.entity.CustomerEntity;
import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.PaymentEntity;
import com.database.petshop.entity.ProductEntity;
import com.database.petshop.entity.StaffEntity;
import com.database.petshop.entity.StatusEntity;
import com.database.petshop.repository.AdminRepository;
import com.database.petshop.repository.CategoryRepository;
import com.database.petshop.repository.CustomerRepository;
import com.database.petshop.repository.OrderDetailRepository;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.PaymentRepository;
import com.database.petshop.repository.ProductRepository;
import com.database.petshop.repository.StaffRepository;
import com.database.petshop.repository.StatusRepository;

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
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (statusRepo.count() == 0) {
            String[] statusNames = {
                "รอชำระเงิน", "รอตรวจสอบยอดเงิน", "ชำระเงินแล้ว",
                "กำลังจัดเตรียมสินค้า", "จัดส่งแล้ว", "ยกเลิก/สลิปไม่ถูกต้อง"
            };
            for (String name : statusNames) {
                StatusEntity s = new StatusEntity();
                s.setStatusName(name);
                statusRepo.save(s);
            }
        }

        if (categoryRepo.count() == 0) {
            String[] catNames = {"อาหารสัตว์", "อุปกรณ์และของใช้", "ของเล่นสัตว์เลี้ยง", "ยาและเวชภัณฑ์"};
            for (String name : catNames) {
                CategoryEntity cat = new CategoryEntity();
                cat.setCategoryName(name);
                categoryRepo.save(cat);
            }
        }

        if (productRepo.count() == 0) {
            CategoryEntity cat1 = categoryRepo.findByCategoryName("อาหารสัตว์");
            CategoryEntity cat2 = categoryRepo.findByCategoryName("อุปกรณ์และของใช้");
            CategoryEntity cat3 = categoryRepo.findByCategoryName("ของเล่นสัตว์เลี้ยง");
            CategoryEntity cat4 = categoryRepo.findByCategoryName("ยาและเวชภัณฑ์");

            saveProduct("อาหารสุนัขเกรดพรีเมียม", new BigDecimal("550.00"), 50, cat1);
            saveProduct("กรงแมวพับได้", new BigDecimal("1200.00"), 10, cat2);
            saveProduct("ไม้ตกแมวขนนก", new BigDecimal("59.00"), 100, cat3);
            saveProduct("แชมพูกำจัดเห็บหมัด", new BigDecimal("250.00"), 30, cat4);
        }

        String pass = passwordEncoder.encode("123456");

        if (adminRepo.count() == 0) {
            saveAdmin("แอดมินหลัก (สมชาย)", "admin1@petshop.com", pass);
            saveAdmin("แอดมินสำรอง (สมหญิง)", "admin2@petshop.com", pass);
        }

        if (staffRepo.count() == 0) {
            saveStaff("พนักงานแพ็กของ 1", "staff1@petshop.com", pass);
            saveStaff("พนักงานแพ็กของ 2", "staff2@petshop.com", pass);
        }

        if (customerRepo.count() == 0) {
            saveCustomer("คุณสมศักดิ์ รักสัตว์", "somsak@email.com", pass, "0811111111");
            saveCustomer("คุณมณี ใจดี", "manee@email.com", pass, "0822222222");
        }

        if (orderRepo.count() == 0) {
            CustomerEntity c1 = customerRepo.findAll().get(0);
            CustomerEntity c2 = customerRepo.findAll().get(1);
            ProductEntity p1 = productRepo.findAll().get(0);
            ProductEntity p2 = productRepo.findAll().get(1);

            OrderEntity o1 = saveOrder(c1, new BigDecimal("550.00"), "รอตรวจสอบยอดเงิน");
            savePayment(o1, new BigDecimal("550.00"), "slip_somsak_01.jpg");

            OrderEntity o2 = saveOrder(c2, new BigDecimal("1200.00"), "ชำระเงินแล้ว");
            saveOrderDetail(o2, p2, 1);

            saveOrder(c1, new BigDecimal("59.00"), "รอชำระเงิน");
        }

        System.out.println(">>> [SUCCESS] Mock Data Initialized with 2 Admins, 2 Staff, 2 Customers, 4 Categories, and 3 Orders!");
    }

    private void saveProduct(String name, BigDecimal price, int stock, CategoryEntity cat) {
        ProductEntity p = new ProductEntity();
        p.setProductName(name);
        p.setPrice(price);
        p.setStock(stock);
        p.setCategory(cat);
        productRepo.save(p);
    }

    private void saveAdmin(String name, String email, String pass) {
        AdminEntity a = new AdminEntity();
        a.setName(name);
        a.setEmail(email);
        a.setPassword(pass);
        adminRepo.save(a);
    }

    private void saveStaff(String name, String email, String pass) {
        StaffEntity s = new StaffEntity();
        s.setName(name);
        s.setEmail(email);
        s.setPassword(pass);
        staffRepo.save(s);
    }

    private void saveCustomer(String name, String email, String pass, String phone) {
        CustomerEntity c = new CustomerEntity();
        c.setCustomerName(name);
        c.setEmail(email);
        c.setPassword(pass);
        c.setPhone(phone);
        customerRepo.save(c);
    }

    private OrderEntity saveOrder(CustomerEntity customer, BigDecimal total, String statusName) {
        OrderEntity o = new OrderEntity();
        o.setCustomer(customer);
        o.setOrderDate(LocalDate.now());
        o.setTotalAmount(total);
        o.setStatus(statusRepo.findByStatusName(statusName));
        return orderRepo.save(o);
    }

    private void savePayment(OrderEntity order, BigDecimal amount, String slip) {
        PaymentEntity p = new PaymentEntity();
        p.setOrder(order);
        p.setAmount(amount);
        p.setMethod("โอนเงิน");
        p.setSlipImage(slip);
        paymentRepo.save(p);
    }

    private void saveOrderDetail(OrderEntity order, ProductEntity product, int qty) {
        OrderDetailEntity d = new OrderDetailEntity();
        d.setOrder(order);
        d.setProduct(product);
        d.setQuantity(qty);
        d.setUnitPrice(product.getPrice());
        orderDetailRepo.save(d);
    }
}
