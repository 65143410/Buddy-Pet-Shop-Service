package com.database.petshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "กรุณาระบุชื่อ-นามสกุล")
    private String customerName;

    @NotBlank(message = "กรุณาระบุอีเมล")
    @Email(message = "รูปแบบอีเมลไม่ถูกต้อง")
    private String email;

    @NotBlank(message = "กรุณาระบุรหัสผ่าน")
    private String password;

    @NotBlank(message = "กรุณาระบุเบอร์โทรศัพท์")
    private String phone;

    private String address;

    // ข้อมูลสัตว์เลี้ยง (Optional)
    private String petName;

    private String petType;

    private String congenitalDisease;
}
