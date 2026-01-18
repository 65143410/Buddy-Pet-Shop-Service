package com.database.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.CustomerEntity;
import com.database.petshop.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<CustomerEntity> findAllCustomers() {
        return customerRepo.findAll();
    }

    public CustomerEntity findCustomerById(Long id) {
        return customerRepo.findById(id).orElse(null);
    }

    public CustomerEntity saveCustomer(CustomerEntity customer) {
        if (customer.getPassword() != null) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        return customerRepo.save(customer);
    }

    public CustomerEntity login(String email, String rawPassword) {
        CustomerEntity customer = customerRepo.findByEmail(email);
        if (customer != null && passwordEncoder.matches(rawPassword, customer.getPassword())) {
            return customer;
        }
        return null;
    }

    public CustomerEntity updateCustomer(Long id, CustomerEntity details) {
        Optional<CustomerEntity> optional = customerRepo.findById(id);
        if (optional.isPresent()) {
            CustomerEntity existing = optional.get();
            existing.setCustomerName(details.getCustomerName());
            existing.setEmail(details.getEmail());
            existing.setPhone(details.getPhone());
            existing.setAddress(details.getAddress());
            existing.setImage(details.getImage());
            if (details.getPassword() != null && !details.getPassword().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(details.getPassword()));
            }
            return customerRepo.save(existing);
        }
        return null;
    }

    public void deleteCustomer(Long id) {
        customerRepo.deleteById(id);
    }

    @Autowired
    private com.database.petshop.repository.PetRepository petRepo;

    @org.springframework.transaction.annotation.Transactional
    public CustomerEntity register(com.database.petshop.dto.RegisterRequest request) {
        
        if (customerRepo.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("อีเมลนี้มีผู้ใช้งานแล้ว");
        }

        
        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerName(request.getCustomerName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setImage(request.getImage());

        CustomerEntity savedCustomer = customerRepo.save(customer);

        
        if (request.getPets() != null && !request.getPets().isEmpty()) {
            for (com.database.petshop.dto.RegisterRequest.PetInfo petInfo : request.getPets()) {
                if (petInfo.getPetName() != null && !petInfo.getPetName().isEmpty()) {
                    com.database.petshop.entity.PetEntity pet = new com.database.petshop.entity.PetEntity();
                    pet.setPetName(petInfo.getPetName());
                    pet.setPetType(petInfo.getPetType());
                    pet.setCongenitalDisease(petInfo.getCongenitalDisease());
                    pet.setBirthdate(petInfo.getPetBirthdate());
                    pet.setWeight(petInfo.getPetWeight());
                    pet.setGender(petInfo.getPetGender());
                    pet.setBreed(petInfo.getPetBreed());
                    pet.setImage(petInfo.getPetImage());
                    pet.setIsSterilized(petInfo.getPetIsSterilized());
                    pet.setCustomer(savedCustomer);
                    petRepo.save(pet);
                }
            }
        } else if (request.getPetName() != null && !request.getPetName().isEmpty()) {
            
            com.database.petshop.entity.PetEntity pet = new com.database.petshop.entity.PetEntity();
            pet.setPetName(request.getPetName());
            pet.setPetType(request.getPetType());
            pet.setCongenitalDisease(request.getCongenitalDisease());
            pet.setBirthdate(request.getPetBirthdate());
            pet.setWeight(request.getPetWeight());
            pet.setGender(request.getPetGender());
            pet.setBreed(request.getPetBreed());
            pet.setImage(request.getPetImage());
            pet.setIsSterilized(request.getPetIsSterilized());
            pet.setCustomer(savedCustomer);
            petRepo.save(pet);
        }

        return savedCustomer;
    }
}
