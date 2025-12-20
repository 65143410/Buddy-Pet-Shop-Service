package com.database.petshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.AdminEntity;
import com.database.petshop.entity.StaffEntity;
import com.database.petshop.repository.AdminRepository;
import com.database.petshop.repository.StaffRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private StaffRepository staffRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminEntity admin = adminRepo.findByEmail(email);
        if (admin != null) {
            return User.withUsername(admin.getEmail())
                    .password(admin.getPassword())
                    .roles("ADMIN")
                    .build();
        }
        StaffEntity staff = staffRepo.findByEmail(email);
        if (staff != null) {
            return User.withUsername(staff.getEmail())
                    .password(staff.getPassword())
                    .roles("STAFF")
                    .build();
        }

        throw new UsernameNotFoundException("ไม่พบผู้ใช้งานด้วยอีเมล: " + email);
    }
}
