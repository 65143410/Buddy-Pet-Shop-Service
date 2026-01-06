package com.database.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.database.petshop.entity.product_logs;

@Repository
public interface ProductLogRepository extends JpaRepository<product_logs, Long> {
    List<product_logs> findByProductIdOrderByTimestampDesc(Long productId);
    
    List<product_logs> findAllByOrderByTimestampDesc();
}