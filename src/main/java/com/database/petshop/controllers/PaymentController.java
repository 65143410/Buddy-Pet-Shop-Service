package com.database.petshop.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.PaymentEntity;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.PaymentRepository;
import com.database.petshop.service.FileService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private FileService fileService;
    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private com.database.petshop.repository.StatusRepository statusRepo;

    @PostMapping("/upload-slip/{orderId}")
    public ResponseEntity<?> uploadSlip(
            @PathVariable Long orderId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("amount") BigDecimal amount) {
        try {
            String fileName = fileService.saveSlip(file);
            OrderEntity order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));
            PaymentEntity payment = new PaymentEntity();
            payment.setOrder(order);
            payment.setAmount(amount);
            payment.setSlipImage(fileName);
            payment.setMethod("Transfer");
            paymentRepo.save(payment);
            com.database.petshop.entity.StatusEntity status = statusRepo.findByStatusName("รอตรวจสอบยอดเงิน");
            if (status != null) {
                order.setStatus(status);
                orderRepo.save(order);
            }
            return ResponseEntity.ok("อัปโหลดสลิปและเปลี่ยนสถานะเป็นรอตรวจสอบยอดเงินเรียบร้อยแล้ว");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }
}
