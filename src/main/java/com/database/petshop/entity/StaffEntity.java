package com.database.petshop.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Staff")
public class StaffEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Staff_ID")
    private Long staffId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Email", unique = true, nullable = false)
    private String email;

    @Column(name = "Password", nullable = false)
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderEntity> managedOrders;

    @Column(name = "status")
    private String status = "ACTIVE";

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<OrderEntity> getManagedOrders() {
        return managedOrders;
    }

    public void setManagedOrders(List<OrderEntity> managedOrders) {
        this.managedOrders = managedOrders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
