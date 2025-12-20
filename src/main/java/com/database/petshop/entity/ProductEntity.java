package com.database.petshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Product")
public class ProductEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_ID")
    private Long productId;

    @Column(name = "Product_Name", nullable = false)
    private String productName;

    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    @Column(name = "Stock", nullable = false)
    private Integer stock;

    @Column(name = "Description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Category_ID", nullable = false)
    private CategoryEntity category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Admin_ID", updatable = false)
    @JsonIgnore
    private AdminEntity admin;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderDetailEntity> orderDetails;

    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public AdminEntity getAdmin() {
        return admin;
    }

    public void setAdmin(AdminEntity admin) {
        this.admin = admin;
    }

    public List<OrderDetailEntity> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailEntity> orderDetails) {
        this.orderDetails = orderDetails;
    }

    
}
