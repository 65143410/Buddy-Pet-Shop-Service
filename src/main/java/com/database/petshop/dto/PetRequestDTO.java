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
}
