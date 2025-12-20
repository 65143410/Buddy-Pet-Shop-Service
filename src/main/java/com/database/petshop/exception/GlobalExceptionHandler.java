package com.database.petshop.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.database.petshop.dto.ErrorMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String technicalMessage = ex.getMostSpecificCause().getMessage();
        String friendlyMessage = "ข้อมูลที่ส่งมาไม่ถูกต้องหรือระบุข้อมูลไม่ครบถ้วน";

// 1. ดักจับกรณีค่าว่าง (Not Null Constraint) สำหรับ DB2
        if (technicalMessage.contains("-407") || technicalMessage.contains("23502")) {
            friendlyMessage = "กรุณาระบุข้อมูลให้ครบถ้วน (พบค่าว่างในฟิลด์ที่จำเป็น เช่น ราคาต่อหน่วย หรือรหัสสินค้า)";
        } // 2. ดักจับกรณีข้อมูลซ้ำ (Duplicate Key) สำหรับ DB2 มักจะเป็นรหัส -803
        else if (technicalMessage.contains("duplicate key") || technicalMessage.contains("-803") || technicalMessage.contains("23505")) {
            friendlyMessage = "ข้อมูลนี้มีอยู่ในระบบแล้ว (ข้อมูลซ้ำ)";
        } // 3. ดักจับกรณี Foreign Key (รหัสลูกค้า/พนักงาน/สินค้า ไม่มีอยู่จริง)
        else if (technicalMessage.contains("Customer_ID") || technicalMessage.contains("CUSTOMER_ID")) {
            friendlyMessage = "ไม่พบข้อมูลลูกค้า หรือรหัสลูกค้าไม่ถูกต้อง";
        } else if (technicalMessage.contains("Product_ID") || technicalMessage.contains("PRODUCT_ID")) {
            friendlyMessage = "ไม่พบรหัสสินค้าที่ระบุในระบบ";
        } else if (technicalMessage.contains("Order_ID") || technicalMessage.contains("ORDER_ID")) {
            friendlyMessage = "ไม่พบรหัสออเดอร์ที่เกี่ยวข้อง";
        } // 4. ดักจับ Constraint อื่นๆ
        else if (technicalMessage.contains("CHECK_STOCK") || technicalMessage.contains("stock_constraint")) {
            friendlyMessage = "ขออภัย สินค้าในสต็อกไม่เพียงพอต่อคำสั่งซื้อนี้";
        } else if (technicalMessage.contains("OrderEntity.status")) {
            friendlyMessage = "กรุณาระบุสถานะ (Status) ของออเดอร์ให้ถูกต้อง";
        }

        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                friendlyMessage,
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                "เกิดข้อผิดพลาดร้ายแรงในระบบ: " + ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errorMessage,
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
