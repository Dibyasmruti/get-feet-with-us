package com.getfeetwithus.service;

import com.getfeetwithus.dto.AdminChangePasswordDto;
import com.getfeetwithus.entity.Admin;
import com.getfeetwithus.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Admin> findByAdminId(String adminId) {
        return adminRepository.findByAdminId(adminId);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Transactional
    public boolean changePassword(String adminId, AdminChangePasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        Admin admin = adminRepository.findByAdminId(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found: " + adminId));

        if (!passwordEncoder.matches(dto.getOldPassword(), admin.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        admin.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        adminRepository.save(admin);
        return true;
    }

    @Transactional
    public boolean resetPassword(String adminIdOrEmail, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        String idStr = adminIdOrEmail.trim();
        Admin admin = adminRepository.findByAdminId(idStr)
                .or(() -> adminRepository.findByEmail(idStr))
                .orElseThrow(() -> new IllegalArgumentException("Admin not found: " + idStr));

        admin.setPasswordHash(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
        return true;
    }
}
