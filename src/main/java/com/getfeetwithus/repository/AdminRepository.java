package com.getfeetwithus.repository;

import com.getfeetwithus.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminId(String adminId);
    Optional<Admin> findByEmail(String email);
    boolean existsByAdminId(String adminId);
}
