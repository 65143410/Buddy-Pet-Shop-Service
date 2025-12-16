package com.database.petshop.repository;

import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, OrderDetailId>{
    
}
