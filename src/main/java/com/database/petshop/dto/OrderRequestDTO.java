package com.database.petshop.dto;

import java.util.List;
import jakarta.validation.Valid; 
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.OrderDetailEntity;

public class OrderRequestDTO {

    @Valid 
    private OrderEntity order; 

    @Valid 
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
}
