package com.database.petshop.dto;

import java.util.List;

import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderEntity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class OrderRequestDTO {

    @Valid
    @NotNull(message = "กรุณาระบุข้อมูลออเดอร์")
    private OrderEntity order;

    @Valid
    @NotNull(message = "กรุณาระบุรายละเอียดสินค้า")
    private List<OrderDetailEntity> details;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public List<OrderDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetailEntity> details) {
        this.details = details;
    }

    private String slipImage;

    public String getSlipImage() {
        return slipImage;
    }

    public void setSlipImage(String slipImage) {
        this.slipImage = slipImage;
    }
}
