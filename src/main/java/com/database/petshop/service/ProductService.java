package com.database.petshop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.ProductEntity;
import com.database.petshop.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    public List<ProductEntity> findAllProduct() {
        return productRepo.findAll();
    }

    public ProductEntity findProductById(Long id) {
        Optional<ProductEntity> product = productRepo.findById(id);
        return product.orElse(null);
    }

    public ProductEntity saveProduct(ProductEntity product) {
        return productRepo.save(product);
    }

    public ProductEntity updateProduct(Long id, ProductEntity productDetails) {
        Optional<ProductEntity> optionalProduct = productRepo.findById(id);

        if (optionalProduct.isPresent()) {
            ProductEntity existingProduct = optionalProduct.get();

            existingProduct.setProductName(productDetails.getProductName());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setStock(productDetails.getStock());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setCategory(productDetails.getCategory());

            return productRepo.save(existingProduct);
        }
        return null;
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    public List<Map<String, Object>> getLowStockAlert(int threshold) {
        
        List<ProductEntity> lowStockProducts = productRepo.findByStockLessThanEqualOrderByStockAsc(threshold);
        return lowStockProducts.stream().map(product -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", product.getProductId());
            map.put("productName", product.getProductName());
            map.put("currentStock", product.getStock());
            map.put("status", (product.getStock() == 0) ? "OUT_OF_STOCK" : "LOW_STOCK");
            return map;
        }).collect(Collectors.toList());
    }
}
