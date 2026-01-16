package com.database.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.database.petshop.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByStockLessThanEqualOrderByStockAsc(Integer threshold);

    @Query("SELECT p FROM ProductEntity p WHERE " +
            "p.isActive = true AND " +
            "((p.targetPetType LIKE CONCAT('%', :type, '%') OR p.targetPetType = 'ALL') AND " +
            "(:disease IS NULL OR p.suitableForDisease LIKE CONCAT('%', :disease, '%') OR p.suitableForDisease = 'ทั่วไป' OR p.suitableForDisease = 'NONE'))")
    List<ProductEntity> findRecommendations(@Param("type") String type, @Param("disease") String disease);

    @Query("SELECT p FROM ProductEntity p WHERE p.isActive = true AND (LOWER(p.targetPetType) LIKE LOWER(CONCAT('%', :type, '%')) OR LOWER(p.targetPetType) = 'all')")
    List<ProductEntity> findActiveProductsByPetType(@Param("type") String type);

}
