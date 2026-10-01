package com.getfeetwithus.service;

import com.getfeetwithus.entity.Admin;
import com.getfeetwithus.entity.User;
import com.getfeetwithus.repository.AdminRepository;
import com.getfeetwithus.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First check Admin repository
        Optional<Admin> adminOpt = adminRepository.findByAdminId(username);
        if (adminOpt.isEmpty()) {
            adminOpt = adminRepository.findByEmail(username);
        }

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (!Boolean.TRUE.equals(admin.getIsActive())) {
                throw new UsernameNotFoundException("Admin account is deactivated: " + username);
            }
            return new org.springframework.security.core.userdetails.User(
                    admin.getAdminId(),
                    admin.getPasswordHash(),
                    Collections.singletonList(new SimpleGrantedAuthority(admin.getRole()))
            );
        }

        // Then check User repository
        Optional<User> userOpt = userRepository.findByUserId(username);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(username);
        }
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByMobileNumber(username);
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!Boolean.TRUE.equals(user.getIsActive())) {
                throw new UsernameNotFoundException("User account is deactivated: " + username);
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUserId(),
                    user.getPasswordHash(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
            );
        }

        throw new UsernameNotFoundException("User/Admin not found with identifier: " + username);
    }
}
