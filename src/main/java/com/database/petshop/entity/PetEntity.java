package com.database.petshop.entity;

import com.ibm.db2.cmx.annotation.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "Pet")
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pet_ID")
    private Long petId;

    @Column(name = "Pet_name", nullable = false)
    private String petName;

    @Column(name = "Pet_type") // เช่น DOG, CAT, BIRD
    private String petType;

    @Column(name = "Congenital_disease") // โรคประจำตัว
    private String congenitalDisease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Customer_ID")
    private CustomerEntity customer;

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

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

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

}
