package com.database.petshop.service;

import java.util.List;
import java.util.Optional;

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
            existingProduct.setAdmin(productDetails.getAdmin());

            return productRepo.save(existingProduct);
        }
        return null;
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
