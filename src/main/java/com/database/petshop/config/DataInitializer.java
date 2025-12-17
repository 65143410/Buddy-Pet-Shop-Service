package com.database.petshop.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            ProductRepository productRepo,
            AdminRepository adminRepo,
            CategoryRepository catRepo,
            StaffRepository staffRepo,   
            StatusRepository statusRepo,
            CustomerRepository customerRepo,
            OrderRepository orderRepo,
            OrderDetailRepository orderDetailRepo,
            PaymentRepository paymentRepo
    ) {
        return args -> {
            
            if (staffRepo.count() == 0) {
                StaffEntity s1 = new StaffEntity();
                s1.setName("พนักงานใจดี สุขสันต์");
                s1.setEmail("staff1@petshop.com");
                s1.setPassword("staffpass1");
                staffRepo.save(s1);

                StaffEntity s2 = new StaffEntity();
                s2.setName("พนักงานขยัน ทำงานดี");
                s2.setEmail("staff2@petshop.com");
                s2.setPassword("staffpass2");
                staffRepo.save(s2);
                System.out.println(">>> Staff Mockup Created!");
            }

            if (adminRepo.count() == 0) {
                AdminEntity a1 = new AdminEntity();
                a1.setName("เอดมินหลัก (สมชาย)");
                a1.setEmail("admin1@petshop.com");
                a1.setPassword("123456");
                adminRepo.save(a1);

                AdminEntity a2 = new AdminEntity();
                a2.setName("เอดมินรอง (สมหญิง)");
                a2.setEmail("admin2@petshop.com");
                a2.setPassword("654321");
                adminRepo.save(a2);
                System.out.println(">>> Admin Mockup Created!");
            }

            if (statusRepo.count() == 0) {
                String[] statuses = {"รอชำระเงิน", "กำลังจัดเตรียมสินค้า", "จัดส่งแล้ว", "ยกเลิก"};
                for (String sName : statuses) {
                    StatusEntity status = new StatusEntity();
                    status.setStatusName(sName);
                    statusRepo.save(status);
                }
                System.out.println(">>> Order Statuses Created!");
            }

            if (customerRepo.count() == 0) {
                CustomerEntity c1 = new CustomerEntity();
                c1.setCustomerName("คุณสมศักดิ์ รักสัตว์");
                c1.setEmail("somsak@email.com");
                c1.setPhone("0812345678");
                c1.setAddress("99/1 ซอยสุขุมวิท กรุงเทพฯ");
                customerRepo.save(c1);
                System.out.println(">>> Customer Mockup Created!");
            }

            if (catRepo.count() == 0) {
                AdminEntity defaultAdmin = adminRepo.findAll().get(0);

                CategoryEntity cat1 = new CategoryEntity();
                cat1.setCategoryName("อาหารสัตว์");
                catRepo.save(cat1);
                createProductsForCategory(productRepo, cat1, defaultAdmin, new String[]{"อาหารสุนัขรสเนื้อ", "อาหารแมวรสปลาทู", "ขนมกระต่าย"}, 150.00);

                CategoryEntity cat2 = new CategoryEntity();
                cat2.setCategoryName("อุปกรณ์ทำความสะอาด");
                catRepo.save(cat2);
                createProductsForCategory(productRepo, cat2, defaultAdmin, new String[]{"แชมพูสุนัข", "ทรายแมวเต้าหู้", "สเปรย์ดับกลิ่น"}, 250.00);

                CategoryEntity cat3 = new CategoryEntity();
                cat3.setCategoryName("ของเล่นสัตว์เลี้ยง");
                catRepo.save(cat3);
                createProductsForCategory(productRepo, cat3, defaultAdmin, new String[]{"ไม้ตกแมว", "ลูกบอลยาง", "คอนโดแมวไซส์ S"}, 80.00);
                System.out.println(">>> Categories and Products Created!");
            }

            if (orderRepo.count() == 0) {
                CustomerEntity customer = customerRepo.findAll().get(0);
                StaffEntity staff = staffRepo.findAll().get(0);
                StatusEntity statusPaid = statusRepo.findAll().get(0); 
                ProductEntity product = productRepo.findAll().get(0);

                OrderEntity order = new OrderEntity();
                order.setCustomer(customer);
                order.setStaff(staff);
                order.setStatus(statusPaid);
                order.setOrderDate(LocalDate.now());
                
                BigDecimal qty = new BigDecimal("2");
                order.setTotalAmount(product.getPrice().multiply(qty));
                
                OrderEntity savedOrder = orderRepo.save(order);

                OrderDetailEntity detail = new OrderDetailEntity();
                detail.setOrder(savedOrder);
                detail.setProduct(product);
                detail.setQuantity(2);
                detail.setUnitPrice(product.getPrice());
                orderDetailRepo.save(detail);

                if (paymentRepo.count() == 0) {
                    PaymentEntity payment = new PaymentEntity();
                    payment.setOrder(savedOrder); 
                    payment.setAmount(savedOrder.getTotalAmount());
                    payment.setMethod("โอนเงินผ่านธนาคาร (Mobile Banking)");
                    payment.setPaymentDate(LocalDateTime.now());
                    paymentRepo.save(payment);
                }

                System.out.println(">>> Mockup Transaction (Order, Detail, Payment) Completed!");
            }
        };
    }

    private void createProductsForCategory(ProductRepository repo, CategoryEntity cat, AdminEntity admin, String[] names, double basePrice) {
        for (int i = 0; i < names.length; i++) {
            ProductEntity p = new ProductEntity();
            p.setProductName(names[i]);
            p.setPrice(BigDecimal.valueOf(basePrice + (i * 50))); 
            p.setStock(20 + (i * 5));
            p.setDescription("รายละเอียดของ " + names[i] + " คุณภาพดีเยี่ยม");
            p.setCategory(cat);
            p.setAdmin(admin);
            repo.save(p);
        }
    }
}