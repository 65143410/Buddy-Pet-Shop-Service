package com.database.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.database.petshop.entity.ProductLog;

@Repository
public interface ProductLogRepository extends JpaRepository<ProductLog, Long> {
    List<ProductLog> findByProductIdOrderByTimestampDesc(Long productId);

    List<ProductLog> findAllByOrderByTimestampDesc();
}