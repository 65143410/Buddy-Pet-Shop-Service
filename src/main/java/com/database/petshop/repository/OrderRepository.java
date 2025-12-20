package com.database.petshop.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.database.petshop.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStaffIsNullAndStatusStatusIdOrderByOrderDateAsc(Long statusId);

    List<OrderEntity> findByStaffStaffIdAndStatusStatusId(Long staffId, Long statusId);

    @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o "
            + "WHERE o.status.statusId = 3 "
            + "AND o.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalSalesByDate(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(o) FROM OrderEntity o "
            + "WHERE o.status.statusId = 3 "
            + "AND o.orderDate BETWEEN :startDate AND :endDate")
    Long countCompletedOrdersByDate(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
