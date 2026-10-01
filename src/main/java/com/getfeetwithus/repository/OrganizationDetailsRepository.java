package com.getfeetwithus.repository;

import com.getfeetwithus.entity.OrganizationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationDetailsRepository extends JpaRepository<OrganizationDetails, Long> {
    Optional<OrganizationDetails> findFirstByOrderByIdAsc();
}
