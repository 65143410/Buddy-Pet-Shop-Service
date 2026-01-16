package com.database.petshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetRequestDTO {

    @NotBlank(message = "กรุณาระบุชื่อสัตว์เลี้ยง")
    private String petName;

    private String petType;

    private String congenitalDisease;

    @NotNull(message = "กรุณาระบุ ID ลูกค้าเจ้าของสัตว์เลี้ยง")
    private Long customerId;

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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    private java.time.LocalDate birthdate;
    private Double weight;
    private String gender;
    private String breed;
    private String image;
    private Boolean isSterilized;

    public java.time.LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(java.time.LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getIsSterilized() {
        return isSterilized;
    }

    public void setIsSterilized(Boolean isSterilized) {
        this.isSterilized = isSterilized;
    }
}
