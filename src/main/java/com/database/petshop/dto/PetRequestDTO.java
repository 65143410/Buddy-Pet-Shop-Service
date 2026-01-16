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
}
