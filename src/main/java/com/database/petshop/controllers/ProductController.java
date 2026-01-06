package com.database.petshop.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.ProductEntity;
import com.database.petshop.entity.product_logs;
import com.database.petshop.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/get-all-product")
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable Long id) {
        ProductEntity product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductEntity> createProduct(@Valid @RequestBody ProductEntity product) {
        ProductEntity savedProduct = productService.saveProduct(product, "Admin");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductEntity productDetails) {
        ProductEntity updatedProduct = productService.updateProduct(id, productDetails, "Admin");
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Map<String, Object>>> getLowStockProducts(
            @RequestParam(defaultValue = "5") int threshold) {
        return ResponseEntity.ok(productService.getLowStockAlert(threshold));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<product_logs>> getAllLogs() {
        // แนะนำให้เรียกผ่าน Service นะครับ
        return ResponseEntity.ok(productService.getAllProductLogs());
    }

    @GetMapping("/logs/{productId}")
    public ResponseEntity<List<product_logs>> getLogsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductLogsById(productId));
    }
}
