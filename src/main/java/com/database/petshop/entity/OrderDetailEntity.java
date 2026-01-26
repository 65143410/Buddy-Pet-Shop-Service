package com.database.petshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Order_Detail")
@IdClass(OrderDetailId.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class OrderDetailEntity implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_ID")
    @JsonIgnore
    private OrderEntity order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Product_ID")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private ProductEntity product;

    @Min(value = 1, message = "จำนวนสินค้าต้องมีอย่างน้อย 1 ชิ้น")
    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Unit_Price", nullable = false)
    private BigDecimal unitPrice;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
