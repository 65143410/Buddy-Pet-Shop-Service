package com.database.petshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Table; // Fixed Import

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

    @Column(name = "Pet_type")
    private String petType;

    @Column(name = "Congenital_disease")
    private String congenitalDisease;

    @Column(name = "Birthdate")
    private java.time.LocalDate birthdate;

    @Column(name = "Weight")
    private Double weight;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Breed")
    private String breed;

    @Column(name = "Image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "Is_Sterilized")
    private Boolean isSterilized = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Customer_ID")
    @JsonBackReference
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

    public java.time.LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(java.time.LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getIsSterilized() {
        return isSterilized;
    }

    public void setIsSterilized(Boolean isSterilized) {
        this.isSterilized = isSterilized;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
}
