package com.database.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.StaffEntity;
import com.database.petshop.repository.StaffRepository;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<StaffEntity> findAllStaff() {
        return staffRepo.findAll();
    }

    public StaffEntity login(String email, String rawPassword) {
        StaffEntity staff = staffRepo.findByEmail(email);
        if (staff != null && passwordEncoder.matches(rawPassword, staff.getPassword())) {
            return staff;
        }
        return null;
    }

    public StaffEntity findStaffById(Long id) {
        return staffRepo.findById(id).orElse(null);
    }

    public StaffEntity saveStaff(StaffEntity staff) {
        return staffRepo.save(staff);
    }

    public StaffEntity updateStaff(Long id, StaffEntity details) {
        return staffRepo.findById(id).map(existing -> {
            existing.setName(details.getName());
            existing.setEmail(details.getEmail());
            if (details.getPassword() != null && !details.getPassword().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(details.getPassword()));
            }
            existing.setPosition(details.getPosition());
            existing.setPhone(details.getPhone());
            existing.setStatus(details.getStatus());
            return staffRepo.save(existing);
        }).orElse(null);
    }

    public void deleteStaff(Long id) {
        staffRepo.deleteById(id);
    }

    public void updateStatus(Long id, String newStatus) {
        StaffEntity staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบพนักงานไอดี: " + id));

        staff.setStatus(newStatus);
        staffRepo.save(staff);
    }
}
