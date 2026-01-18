package com.database.petshop.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.database.petshop.entity.ProductEntity;
import com.database.petshop.entity.ProductLog;
import com.database.petshop.repository.ProductLogRepository;
import com.database.petshop.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    @Autowired
    private ProductLogRepository productLogRepo;

    public List<ProductEntity> findAllProduct() {
        return productRepo.findAll();
    }

    public ProductEntity findProductById(Long id) {
        Optional<ProductEntity> product = productRepo.findById(id);
        return product.orElse(null);
    }

    public ProductEntity saveProduct(ProductEntity product) {
        return saveProduct(product, "System"); 
    }

    public ProductEntity updateProduct(Long id, ProductEntity productDetails) {
        return updateProduct(id, productDetails, "System");
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

    public List<ProductLog> getAllProductLogs() {
        return productLogRepo.findAll();
    }

    public List<ProductLog> getProductLogsById(Long productId) {
        return productLogRepo.findByProductIdOrderByTimestampDesc(productId);
    }

    @Transactional
    public ProductEntity saveProduct(ProductEntity product, String staffName) {

        ProductEntity savedProduct = productRepo.save(product);

        ProductLog log = new ProductLog();
        log.setProductId(savedProduct.getProductId());
        log.setProductName(savedProduct.getProductName());
        log.setAction("ADD");
        log.setQuantityChange(savedProduct.getStock());
        log.setFinalStock(savedProduct.getStock());
        log.setTimestamp(LocalDateTime.now());
        log.setNotes("เพิ่มสินค้าใหม่เข้าสู่ระบบ");
        log.setStaffName(staffName);

        productLogRepo.save(log);

        return savedProduct;
    }

    @Transactional
    public ProductEntity updateProduct(Long id, ProductEntity productDetails, String staffName) {
        Optional<ProductEntity> optionalProduct = productRepo.findById(id);

        if (optionalProduct.isPresent()) {
            ProductEntity existingProduct = optionalProduct.get();

            int oldStock = existingProduct.getStock();
            int newStock = productDetails.getStock();

            existingProduct.setProductName(productDetails.getProductName());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setStock(newStock);
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setCategory(productDetails.getCategory());

            existingProduct.setIsActive(productDetails.getIsActive());
            existingProduct.setBrand(productDetails.getBrand());
            existingProduct.setWeightVolume(productDetails.getWeightVolume());

            ProductEntity updatedProduct = productRepo.save(existingProduct);

            ProductLog log = new ProductLog();
            log.setProductId(updatedProduct.getProductId());
            log.setProductName(updatedProduct.getProductName());
            log.setAction("UPDATE");
            log.setQuantityChange(newStock - oldStock);
            log.setFinalStock(updatedProduct.getStock());
            log.setTimestamp(LocalDateTime.now());
            log.setNotes("อัปเดตข้อมูล/สต็อกสินค้า");
            log.setStaffName(staffName);

            productLogRepo.save(log);

            return updatedProduct;
        }
        return null;
    }

    @Transactional
    public void deleteProduct(Long id) {

        ProductEntity product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบสินค้า ID: " + id));

        
        product.setIsActive(false);
        productRepo.save(product);

        ProductLog log = new ProductLog();
        log.setProductId(product.getProductId());
        log.setProductName(product.getProductName());
        log.setAction("DEACTIVATE");
        log.setQuantityChange(0);
        log.setFinalStock(product.getStock());
        log.setStaffName("Admin");
        log.setTimestamp(LocalDateTime.now());
        log.setNotes("ปิดการขายสินค้า (Soft Delete)");

        productLogRepo.save(log);

    }

    public List<ProductEntity> getRecommendedProducts(String petType, String disease) {
        
        List<ProductEntity> allProducts = productRepo.findActiveProductsByPetType(petType);

        if (disease == null || disease.equalsIgnoreCase("NONE")) {
            
            
            
            
            
            
            return allProducts;
        }

        
        
        return allProducts.stream()
                .sorted((p1, p2) -> {
                    String d1 = p1.getSuitableForDisease();
                    String d2 = p2.getSuitableForDisease();

                    boolean p1Matches = d1 != null && d1.equalsIgnoreCase(disease);
                    boolean p2Matches = d2 != null && d2.equalsIgnoreCase(disease);

                    
                    if (p1Matches && !p2Matches)
                        return -1; 
                    if (!p1Matches && p2Matches)
                        return 1; 
                    return 0;
                })
                .collect(Collectors.toList());
    }
}
