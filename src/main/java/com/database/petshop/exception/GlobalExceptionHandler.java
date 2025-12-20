package com.database.petshop.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        if (technicalMessage.contains("OrderEntity.status")) {
            friendlyMessage = "กรุณาระบุสถานะ (Status) ของออเดอร์ให้ถูกต้อง";
        } else if (technicalMessage.contains("Customer_ID")) {
            friendlyMessage = "ไม่พบข้อมูลลูกค้า หรือไอดีลูกค้าไม่ถูกต้อง";
        } else if (technicalMessage.contains("duplicate key")) {
            friendlyMessage = "ข้อมูลนี้มีอยู่ในระบบแล้ว (ข้อมูลซ้ำ)";
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
}
