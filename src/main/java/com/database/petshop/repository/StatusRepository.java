package com.database.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.database.petshop.entity.StatusEntity;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    StatusEntity findByStatusName(String statusName);
}
