package com.database.petshop.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable Long id) {
        ProductEntity product = productService.findProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/add")
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {
        try {
            ProductEntity savedProduct = productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @RequestBody ProductEntity productDetails) {
        try {
            ProductEntity updatedProduct = productService.updateProduct(id, productDetails);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
