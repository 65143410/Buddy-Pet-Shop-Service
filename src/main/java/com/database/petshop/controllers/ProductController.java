package com.database.petshop.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.ProductEntity;
import com.database.petshop.service.ProductService;


@RestController
@RequestMapping("/api/product")
public class ProductController {
    
    @Autowired
    ProductService productService;

    @GetMapping("/get-all-product")
    public List<ProductEntity> findAllIncomeDeductInfo() {
        List<ProductEntity> res = new ArrayList<ProductEntity>();
        try {
            res = productService.findAllProduct();
        } catch (Exception e) {
            System.out.println("can not find product info");
            return null;
        }

        return res;
    }


    // @GetMapping("/save-product")
    // public List<Object> findAllIncomeDeductInfo() {
    //     List<Object> res = new ArrayList<>();
    //     try {
    //         res = incDeductService.findAllIncomeDeductInfo();
    //     } catch (Exception e) {
    //         System.out.println("can not find income-deduct info");
    //         return null;
    //     }

    //     return res;
    // }
}
