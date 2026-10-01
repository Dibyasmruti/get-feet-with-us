package com.getfeetwithus.repository;

import com.getfeetwithus.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByResetToken(String resetToken);
    Optional<PasswordResetToken> findByIdentifierAndUserTypeAndIsUsedFalse(String identifier, String userType);
}
