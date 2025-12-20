package com.database.petshop.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CancelOrder")
public class CancelOrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Cancel_ID")
    private Long cancelId;

    @Column(name = "Cancel_Date", nullable = false)
    private LocalDateTime cancelDate = LocalDateTime.now();

    @Column(name = "Reason")
    private String reason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_ID", nullable = false, unique = true) 
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Staff_ID")
    @JsonIgnore
    private StaffEntity staff;

    public Long getCancelId() {
        return cancelId;
    }

    public void setCancelId(Long cancelId) {
        this.cancelId = cancelId;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    
}
