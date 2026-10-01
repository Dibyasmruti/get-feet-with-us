package com.getfeetwithus.repository;

import com.getfeetwithus.entity.OrderAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderAddressRepository extends JpaRepository<OrderAddress, Long> {
    Optional<OrderAddress> findByOrder_OrderId(String orderId);
}
