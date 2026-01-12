package com.database.petshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.PetEntity;
import com.database.petshop.repository.PetRepository;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepo;

    public PetEntity savePet(PetEntity pet) {
        return petRepo.save(pet);
    }

    public List<PetEntity> getPetsByCustomerId(Long customerId) {
        return petRepo.findByCustomer_CustomerId(customerId);
    }

    public void deletePet(Long petId) {
        petRepo.deleteById(petId);
    }
}
