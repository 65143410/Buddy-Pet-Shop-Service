package com.database.petshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.StatusEntity;
import com.database.petshop.repository.StatusRepository;

@Service
public class StatusService {
    @Autowired private StatusRepository statusRepo;

    public List<StatusEntity> findAllStatuses() { return statusRepo.findAll(); }
}
