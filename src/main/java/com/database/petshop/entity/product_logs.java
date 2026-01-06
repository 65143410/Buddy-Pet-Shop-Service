package com.database.petshop.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product_logs")
@Data
public class product_logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Log_ID")
    private Long logId;

    @Column(name = "Product_ID")
    private Long productId;

    @Column(name = "Product_Name")
    private String productName;

    @Column(name = "Action")
    private String action;

    @Column(name = "Quantity_Change")
    private Integer quantityChange;

    @Column(name = "Final_Stock")
    private Integer finalStock;

    @Column(name = "Staff_Name")
    private String staffName;

    @Column(name = "Notes")
    private String notes;

    @Column(name = "Timestamp")
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

}
