package com.database.petshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.dto.request.SlipRequest;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.PaymentEntity;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.PaymentRepository;
import com.database.petshop.service.FileService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

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

    @PostMapping("/upload-slip")

    public ResponseEntity<?> uploadSlip(
            @RequestBody SlipRequest request) {
        try {
            OrderEntity order = orderRepo.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));

            PaymentEntity payment = new PaymentEntity();
            payment.setOrder(order);
            payment.setAmount(request.getAmount());
            payment.setSlipImage(request.getFile());
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
