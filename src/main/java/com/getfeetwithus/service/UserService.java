package com.getfeetwithus.service;

import com.getfeetwithus.dto.UserChangePasswordDto;
import com.getfeetwithus.dto.UserForgotPasswordDto;
import com.getfeetwithus.dto.UserRegistrationDto;
import com.getfeetwithus.dto.UserSettingsDto;
import com.getfeetwithus.entity.PasswordResetToken;
import com.getfeetwithus.entity.User;
import com.getfeetwithus.entity.UserAddress;
import com.getfeetwithus.repository.PasswordResetTokenRepository;
import com.getfeetwithus.repository.UserAddressRepository;
import com.getfeetwithus.repository.UserRepository;
import com.getfeetwithus.util.IdGeneratorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGeneratorUtil idGeneratorUtil;

    public UserService(UserRepository userRepository,
                       UserAddressRepository userAddressRepository,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       PasswordEncoder passwordEncoder,
                       IdGeneratorUtil idGeneratorUtil) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.idGeneratorUtil = idGeneratorUtil;
    }

    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        if (userRepository.existsByMobileNumber(dto.getMobileNumber())) {
            throw new IllegalArgumentException("Mobile number is already registered");
        }

        User user = new User();
        user.setUserId(idGeneratorUtil.generateUserId());
        user.setName(dto.getName().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setMobileNumber(dto.getMobileNumber().trim());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress().trim());
        user.setPinCode(dto.getPinCode().trim());
        user.setIsActive(true);
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Save default address
        UserAddress address = new UserAddress();
        address.setUserId(savedUser.getUserId());
        address.setAddressLine(savedUser.getAddress());
        address.setPinCode(savedUser.getPinCode());
        address.setIsDefault(true);
        userAddressRepository.save(address);

        return savedUser;
    }

    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public boolean changePassword(String userId, UserChangePasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean resetPassword(UserForgotPasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        String idStr = dto.getIdentifier().trim();
        User user = userRepository.findByUserId(idStr)
                .or(() -> userRepository.findByEmail(idStr))
                .or(() -> userRepository.findByMobileNumber(idStr))
                .orElseThrow(() -> new IllegalArgumentException("User account not found with identifier: " + idStr));

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public void updateUserSettings(String userId, UserSettingsDto dto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.setAddress(dto.getAddress().trim());
        user.setPinCode(dto.getPinCode().trim());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Update default address
        Optional<UserAddress> addrOpt = userAddressRepository.findByUserIdAndIsDefaultTrue(userId);
        if (addrOpt.isPresent()) {
            UserAddress addr = addrOpt.get();
            addr.setAddressLine(dto.getAddress().trim());
            addr.setPinCode(dto.getPinCode().trim());
            userAddressRepository.save(addr);
        } else {
            UserAddress addr = new UserAddress();
            addr.setUserId(userId);
            addr.setAddressLine(dto.getAddress().trim());
            addr.setPinCode(dto.getPinCode().trim());
            addr.setIsDefault(true);
            userAddressRepository.save(addr);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchUsers(String query) {
        if (query == null || query.isBlank()) {
            return getAllUsers();
        }
        String q = query.trim();
        return userRepository.findByNameContainingIgnoreCaseOrUserIdContainingIgnoreCaseOrEmailContainingIgnoreCaseOrMobileNumberContaining(q, q, q, q);
    }

    @Transactional
    public void toggleUserStatus(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
