package com.database.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.CustomerEntity;
import com.database.petshop.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public List<CustomerEntity> findAllCustomers() {
        return customerRepo.findAll();
    }

    public CustomerEntity findCustomerById(Long id) {
        return customerRepo.findById(id).orElse(null);
    }

    public CustomerEntity saveCustomer(CustomerEntity customer) {
        return customerRepo.save(customer);
    }
    public CustomerEntity updateCustomer(Long id, CustomerEntity details) {
        Optional<CustomerEntity> optional = customerRepo.findById(id);
        if (optional.isPresent()) {
            CustomerEntity existing = optional.get();
            existing.setCustomerName(details.getCustomerName());
            existing.setEmail(details.getEmail());
            existing.setPhone(details.getPhone());
            existing.setAddress(details.getAddress());
            return customerRepo.save(existing);
        }
        return null;
    }
    public void deleteCustomer(Long id) {
        customerRepo.deleteById(id);
    }
}
