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

    @Autowired
    private com.database.petshop.repository.CustomerRepository customerRepo;

    public PetEntity createPet(com.database.petshop.dto.PetRequestDTO dto) {
        com.database.petshop.entity.CustomerEntity customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("ไม่พบลูกค้า ID: " + dto.getCustomerId()));

        PetEntity pet = new PetEntity();
        pet.setPetName(dto.getPetName());
        pet.setPetType(dto.getPetType());
        pet.setCongenitalDisease(dto.getCongenitalDisease());

        pet.setBirthdate(dto.getBirthdate());
        pet.setWeight(dto.getWeight());
        pet.setGender(dto.getGender());
        pet.setBreed(dto.getBreed());
        pet.setImage(dto.getImage());
        pet.setIsSterilized(dto.getIsSterilized());
        pet.setCustomer(customer);

        return petRepo.save(pet);
    }

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
