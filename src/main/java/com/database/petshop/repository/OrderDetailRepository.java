package com.database.petshop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderDetailId;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, OrderDetailId> {

    @Query("SELECT od.product, SUM(od.quantity) as totalQty FROM OrderDetailEntity od WHERE od.order.status.statusId = 5 GROUP BY od.product ORDER BY totalQty DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);
}
