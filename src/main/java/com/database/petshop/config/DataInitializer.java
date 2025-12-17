package com.database.petshop.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.entity.CategoryEntity;
import com.database.petshop.entity.ProductEntity;
import com.database.petshop.repository.AdminRepository;
import com.database.petshop.repository.CategoryRepository;
import com.database.petshop.repository.ProductRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            ProductRepository productRepo,
            AdminRepository adminRepo,
            CategoryRepository catRepo
    ) {
        return args -> {

            // 1. ต้องสร้าง Admin ก่อน (เพราะ Product ต้องการ Admin_ID)
            AdminEntity admin = new AdminEntity();
            if (adminRepo.count() == 0) {
                admin.setName("เอดมินหลัก ฉันเอง");
                admin.setEmail("admin@petshop.com");
                admin.setPassword("123456");
                admin = adminRepo.save(admin); // บันทึกและเก็บ object ที่มี ID ไว้ใช้งาน
            } else {
                admin = adminRepo.findAll().get(0); // ถ้ามีอยู่แล้วให้ดึงตัวแรกมา
            }

            // 2. ต้องสร้าง Category ก่อน (เพราะ Product ตั้งค่า nullable = false ใน Category_ID)
            CategoryEntity category = new CategoryEntity();
            if (catRepo.count() == 0) {
                category.setCategoryName("อาหารสัตว์ทั่วไป");
                category = catRepo.save(category); // บันทึกและเก็บ object ที่มี ID ไว้ใช้งาน
            } else {
                category = catRepo.findAll().get(0); // ถ้ามีอยู่แล้วให้ดึงตัวแรกมา
            }

            // 3. สร้าง Product โดยเอา Admin และ Category มาใส่
            if (productRepo.count() == 0) {
                ProductEntity product = new ProductEntity();
                // product.setProductId(1L); // ไม่ต้องเซ็ต ID เองเพราะเป็น IDENTITY
                product.setProductName("อาหารแมวเกรดส่งออก");
                product.setPrice(new BigDecimal("1250.75"));
                product.setStock(10);
                product.setDescription("สูตรพรีเมียมบำรุงขน ฉันเอง");
                
                // --- จุดสำคัญ: การ Join ---
                product.setCategory(category); // ใส่ object category ที่บันทึกแล้ว
                product.setAdmin(admin);       // ใส่ object admin ที่บันทึกแล้ว
                
                productRepo.save(product);
                System.out.println(">>> Mockup Data Created Successfully!");
            }
        };
    }
}