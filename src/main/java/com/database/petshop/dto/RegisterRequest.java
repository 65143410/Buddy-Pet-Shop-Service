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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getCongenitalDisease() {
        return congenitalDisease;
    }

    public void setCongenitalDisease(String congenitalDisease) {
        this.congenitalDisease = congenitalDisease;
    }

    private String image;
    private java.time.LocalDate petBirthdate;
    private Double petWeight;
    private String petGender;
    private String petBreed;
    private String petImage;
    private Boolean petIsSterilized;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public java.time.LocalDate getPetBirthdate() {
        return petBirthdate;
    }

    public void setPetBirthdate(java.time.LocalDate petBirthdate) {
        this.petBirthdate = petBirthdate;
    }

    public Double getPetWeight() {
        return petWeight;
    }

    public void setPetWeight(Double petWeight) {
        this.petWeight = petWeight;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getPetImage() {
        return petImage;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;
    }

    public Boolean getPetIsSterilized() {
        return petIsSterilized;
    }

    public void setPetIsSterilized(Boolean petIsSterilized) {
        this.petIsSterilized = petIsSterilized;
    }
}
