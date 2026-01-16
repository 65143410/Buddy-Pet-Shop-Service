package com.database.petshop.dto;

import java.util.List;

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
    private String image;

    // Support for multiple pets
    private List<PetInfo> pets;

    @Data
    public static class PetInfo {
        private String petName;
        private String petType;
        private String congenitalDisease;
        private java.time.LocalDate petBirthdate;
        private Double petWeight;
        private String petGender;
        private String petBreed;
        private String petImage;
        private Boolean petIsSterilized;
    }

    // Deprecated single pet fields (kept for backward compatibility if needed, but
    // primary logic will check 'pets' list)
    private String petName;
    private String petType;
    private String congenitalDisease;
    private java.time.LocalDate petBirthdate;
    private Double petWeight;
    private String petGender;
    private String petBreed;
    private String petImage;
    private Boolean petIsSterilized;
}
