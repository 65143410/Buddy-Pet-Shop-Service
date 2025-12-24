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

    List<OrderEntity> findByStatus_StatusId(Long statusId);

    List<OrderEntity> findByCustomerCustomerIdOrderByOrderDateDesc(Long customerId);

    List<OrderEntity> findByStaffIsNullAndStatusStatusIdOrderByOrderDateAsc(Long statusId);

    List<OrderEntity> findByStaffStaffIdAndStatusStatusId(Long staffId, Long statusId);

    List<OrderEntity> findByCustomer_CustomerNameContainingIgnoreCaseOrderByOrderDateDesc(String customerName);

    List<OrderEntity> findByStatus_StatusName(String statusName);

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

    @Query("SELECT o FROM OrderEntity o WHERE LOWER(o.customer.customerName) LIKE LOWER(CONCAT('%', :customerName, '%')) ORDER BY o.orderDate DESC")
    List<OrderEntity> searchByCustomerName(@Param("customerName") String customerName);

    @Query("SELECT o FROM OrderEntity o WHERE o.status.statusName = 'ชำระเงินแล้ว'")
    List<OrderEntity> findPaidOrdersForStaff();

    @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o")
    Double sumTotalAmount();
}
