package com.database.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.database.petshop.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
    List<ProductEntity> findByStockLessThanEqualOrderByStockAsc(Integer threshold);
}
