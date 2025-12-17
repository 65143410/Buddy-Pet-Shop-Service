package com.database.petshop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.ProductEntity;
import com.database.petshop.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    public List<ProductEntity> findAllProduct(){
        List<ProductEntity> result = new ArrayList<ProductEntity>();
        result = productRepo.findAll();
        System.out.println("TEST");
        return result;
    }
}
