package com.getfeetwithus.repository;

import com.getfeetwithus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByMobileNumber(String mobileNumber);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);
    List<User> findByNameContainingIgnoreCaseOrUserIdContainingIgnoreCaseOrEmailContainingIgnoreCaseOrMobileNumberContaining(String name, String userId, String email, String mobile);
}
