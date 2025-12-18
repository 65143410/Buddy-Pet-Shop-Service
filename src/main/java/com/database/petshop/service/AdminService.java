package com.database.petshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.repository.AdminRepository;

@Service
public class AdminService {
    @Autowired private AdminRepository adminRepo;

    public List<AdminEntity> findAllAdmins() { return adminRepo.findAll(); }
    public AdminEntity saveAdmin(AdminEntity admin) { return adminRepo.save(admin); }
}
