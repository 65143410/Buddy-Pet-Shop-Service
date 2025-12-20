package com.database.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.database.petshop.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStaffIsNullAndStatusStatusIdOrderByOrderDateAsc(Long statusId);

    List<OrderEntity> findByStaffStaffIdAndStatusStatusId(Long staffId, Long statusId);
}
