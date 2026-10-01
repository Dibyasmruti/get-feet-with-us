package com.getfeetwithus.repository;

import com.getfeetwithus.entity.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {
    Optional<ProductMaster> findByProductId(String productId);
    List<ProductMaster> findByIsActiveTrue();
    List<ProductMaster> findAllByOrderByCreatedAtDesc();
}
