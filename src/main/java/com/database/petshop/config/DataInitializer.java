package com.database.petshop.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    @Autowired
    private CancelOrderRepository cancelOrderRepo;

    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (statusRepo.count() > 0) {
            return;
        }

        String[] statusNames = {"รอชำระเงิน", "รอตรวจสอบยอดเงิน", "ชำระเงินแล้ว", "กำลังจัดเตรียมสินค้า", "จัดส่งแล้ว", "ยกเลิก/สลิปไม่ถูกต้อง"};
        for (String name : statusNames) {
            StatusEntity s = new StatusEntity();
            s.setStatusName(name);
            statusRepo.save(s);
        }

        String[] catNames = {"อาหารสัตว์", "อุปกรณ์และของใช้", "ของเล่นสัตว์เลี้ยง", "ยาและเวชภัณฑ์"};
        for (String name : catNames) {
            CategoryEntity cat = new CategoryEntity();
            cat.setCategoryName(name);
            categoryRepo.save(cat);
        }

        String pass = passwordEncoder.encode("123456");
        AdminEntity admin = new AdminEntity();
        admin.setName("แอดมินสมชาย");
        admin.setEmail("admin1@petshop.com");
        admin.setPassword(pass);
        adminRepo.save(admin);

        StaffEntity staff1 = saveStaff("พนักงานวิภา", "staff1@petshop.com", pass);
        StaffEntity staff2 = saveStaff("พนักงานมานะ", "staff2@petshop.com", pass);

        CategoryEntity cat1 = categoryRepo.findByCategoryName("อาหารสัตว์");
        saveProduct("อาหารสุนัขโต Premium 10kg", new BigDecimal("1550"), 50, "สารอาหารครบถ้วน", cat1, admin);
        saveProduct("อาหารแมวเปียก (12 ซอง)", new BigDecimal("240"), 100, "รสปลาทูน่าและไก่", cat1, admin);
        saveProduct("ขนมขัดฟันสุนัข XL", new BigDecimal("350"), 80, "ช่วยลดคราบหินปูน", cat1, admin);
        saveProduct("นมแพะสำหรับลูกสัตว์ 400ml", new BigDecimal("65"), 200, "ย่อยง่าย แคลเซียมสูง", cat1, admin);
        saveProduct("อาหารเม็ดแมวสูตร Indoor 2kg", new BigDecimal("490"), 40, "ลดกลิ่นมูลและก้อนขน", cat1, admin);

        CategoryEntity cat2 = categoryRepo.findByCategoryName("อุปกรณ์และของใช้");
        saveProduct("สายจูงรัดอกสะท้อนแสง", new BigDecimal("290"), 30, "ปรับสายได้ แข็งแรง", cat2, admin);
        saveProduct("กระบะทรายแมวทรงโดม", new BigDecimal("850"), 15, "กันทรายกระเด็นและเก็บกลิ่น", cat2, admin);
        saveProduct("หวีแปรงขนสแตนเลส", new BigDecimal("150"), 50, "ลดการพันกันของเส้นขน", cat2, admin);
        saveProduct("ชามอาหารคู่สแตนเลส", new BigDecimal("320"), 25, "มียางกันลื่นใต้ฐาน", cat2, admin);
        saveProduct("ที่นอนนุ่มฟูทรงวงกลม", new BigDecimal("550"), 10, "ซักทำความสะอาดได้", cat2, admin);

        CategoryEntity cat3 = categoryRepo.findByCategoryName("ของเล่นสัตว์เลี้ยง");
        saveProduct("บอลยางมีเสียงตะมุตะมิ", new BigDecimal("89"), 100, "ทนต่อการกัด", cat3, admin);
        saveProduct("ไม้ตกแมวขนนกพรีเมียม", new BigDecimal("120"), 60, "ดึงดูดสัญชาตญาณนักล่า", cat3, admin);
        saveProduct("อุโมงค์แมว 3 ทางพับได้", new BigDecimal("450"), 20, "พื้นที่เล่นซ่อนหา", cat3, admin);
        saveProduct("เชือกถักขัดฟันสำหรับสุนัข", new BigDecimal("190"), 45, "ฝ้ายธรรมชาติ 100%", cat3, admin);
        saveProduct("คอนโดแมว 3 ชั้น", new BigDecimal("1850"), 5, "มีที่ลับเล็บในตัว", cat3, admin);

        CategoryEntity cat4 = categoryRepo.findByCategoryName("ยาและเวชภัณฑ์");
        saveProduct("ยาหยอดกำจัดเห็บหมัด", new BigDecimal("220"), 100, "ป้องกันได้นาน 1 เดือน", cat4, admin);
        saveProduct("น้ำยาเช็ดทำความสะอาดหู", new BigDecimal("180"), 50, "ลดกลิ่นอับและแบคทีเรีย", cat4, admin);
        saveProduct("วิตามินบำรุงเลือดและขน", new BigDecimal("450"), 30, "ชนิดเม็ดทานง่าย", cat4, admin);
        saveProduct("สเปรย์รักษาแผลสดสัตว์เลี้ยง", new BigDecimal("280"), 40, "ไม่แสบ แห้งไว", cat4, admin);
        saveProduct("ผงโปรไบโอติกส์เสริมภูมิ", new BigDecimal("590"), 25, "ผสมในอาหาร ช่วยระบบย่อย", cat4, admin);

        saveCustomer("สมศักดิ์ รักสัตว์", "somsak@email.com", pass, "0811111111", "123/4 ม.5 จ.ชลบุรี 20000");
        saveCustomer("มณี ใจดี", "manee@email.com", pass, "0822222222", "99/1 ซ.อารีย์ กทม. 10400");
        saveCustomer("ธนา รวยมาก", "thana@email.com", pass, "0833333333", "8/88 ถ.นิมมาน จ.เชียงใหม่ 50200");
        saveCustomer("วิไล พึ่งพา", "wilai@email.com", pass, "0844444444", "45 ถ.มิตรภาพ จ.ขอนแก่น 40000");
        saveCustomer("จอย จอมแก่น", "joy@email.com", pass, "0855555555", "77 หมู่บ้านเพชร จ.นนทบุรี 11000");
        saveCustomer("กิตติศักดิ์ มั่นคง", "kitti@email.com", pass, "0866666666", "21/2 ถ.พระราม 2 กทม. 10150");
        saveCustomer("นารี รัตนา", "naree@email.com", pass, "0877777777", "303 หอพักสบาย จ.ปทุมธานี 12120");
        saveCustomer("ปกรณ์ ประเสริฐ", "pakorn@email.com", pass, "0888888888", "10/1 ถ.สุขุมวิท จ.ระยอง 21000");
        saveCustomer("รัตนา มานะ", "rattana@email.com", pass, "0899999999", "555 ถ.สีลม เขตบางรัก กทม. 10500");
        saveCustomer("สุวิทย์ วิทยฐานะ", "suwit@email.com", pass, "0800000000", "9/9 ถ.สุรนารี จ.นครราชสีมา 30000");

        List<CustomerEntity> customers = customerRepo.findAll();
        List<ProductEntity> products = productRepo.findAll();
        Random random = new Random();

        for (int i = 1; i <= 80; i++) {
            int daysAgo = random.nextInt(60);
            LocalDate oDate = LocalDate.now().minusDays(daysAgo);
            CustomerEntity customer = customers.get(random.nextInt(customers.size()));

            int itemsInOrder = random.nextInt(2) + 1;
            BigDecimal totalOrderAmount = BigDecimal.ZERO;
            List<OrderDetailEntity> details = new ArrayList<>();

            for (int j = 0; j < itemsInOrder; j++) {
                ProductEntity product = products.get(random.nextInt(products.size()));
                int qty = random.nextInt(3) + 1;
                BigDecimal lineTotal = product.getPrice().multiply(new BigDecimal(qty));
                totalOrderAmount = totalOrderAmount.add(lineTotal);

                OrderDetailEntity detail = new OrderDetailEntity();
                detail.setProduct(product);
                detail.setQuantity(qty);
                detail.setUnitPrice(product.getPrice());
                details.add(detail);
            }

            String statusName;
            if (daysAgo > 20) {
                statusName = (random.nextInt(10) > 1) ? "จัดส่งแล้ว" : "ยกเลิก/สลิปไม่ถูกต้อง";
            } else if (daysAgo > 7) {
                statusName = "กำลังจัดเตรียมสินค้า";
            } else {
                statusName = statusNames[random.nextInt(3)];
            }
            OrderEntity order = new OrderEntity();
            order.setCustomer(customer);
            order.setOrderDate(oDate);
            order.setTotalAmount(totalOrderAmount);
            order.setStatus(statusRepo.findByStatusName(statusName));
            order.setStaff(statusName.contains("จัด") ? staff1 : null);
            order.setInvoiceNo("INV-" + oDate.getYear() + String.format("%02d%04d", oDate.getMonthValue(), i));
            orderRepo.save(order);

            for (OrderDetailEntity d : details) {
                d.setOrder(order);
                orderDetailRepo.save(d);
            }

            if (!statusName.equals("รอชำระเงิน")) {
                savePayment(order, totalOrderAmount, oDate, random);
            }

            if (statusName.equals("ยกเลิก/สลิปไม่ถูกต้อง")) {
                saveCancelOrder(order, daysAgo, staff2);
            }
        }
        System.out.println(">>> [SUCCESS] Mock Data Initialized: 20 Products, 10 Customers, 80 Orders.");
    }

    private void savePayment(OrderEntity order, BigDecimal amount, LocalDate date, Random r) {
        PaymentEntity p = new PaymentEntity();
        p.setOrder(order);
        p.setAmount(amount);
        p.setMethod("โอนเงิน");
        p.setPaymentDate(date.atTime(r.nextInt(23), r.nextInt(59)));
        paymentRepo.save(p);
    }

    private void saveCancelOrder(OrderEntity order, int daysAgo, StaffEntity staff) {
        CancelOrderEntity cancel = new CancelOrderEntity();
        cancel.setOrder(order);
        cancel.setReason("ข้อมูลการโอนเงินไม่ชัดเจน");
        cancel.setCancelDate(LocalDateTime.now().minusDays(daysAgo));
        cancel.setStaff(staff);
        cancelOrderRepo.save(cancel);
    }

    private void saveProduct(String name, BigDecimal price, int stock, String desc, CategoryEntity cat, AdminEntity admin) {
        ProductEntity p = new ProductEntity();
        p.setProductName(name);
        p.setPrice(price);
        p.setStock(stock);
        p.setDescription(desc);
        p.setCategory(cat);
        p.setAdmin(admin);
        productRepo.save(p);
    }

    private void saveCustomer(String name, String email, String pass, String phone, String addr) {
        CustomerEntity c = new CustomerEntity();
        c.setCustomerName(name);
        c.setEmail(email);
        c.setPassword(pass);
        c.setPhone(phone);
        c.setAddress(addr);
        customerRepo.save(c);
    }

    private StaffEntity saveStaff(String name, String email, String pass) {
        StaffEntity s = new StaffEntity();
        s.setName(name);
        s.setEmail(email);
        s.setPassword(pass);
        return staffRepo.save(s);
    }
}
