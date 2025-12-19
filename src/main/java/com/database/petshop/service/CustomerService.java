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

}
