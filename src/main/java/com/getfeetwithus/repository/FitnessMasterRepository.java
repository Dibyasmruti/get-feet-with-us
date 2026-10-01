package com.getfeetwithus.repository;

import com.getfeetwithus.entity.FitnessMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessMasterRepository extends JpaRepository<FitnessMaster, Long> {
    Optional<FitnessMaster> findByFitnessId(String fitnessId);
    List<FitnessMaster> findByIsActiveTrue();
    List<FitnessMaster> findAllByOrderByCreatedAtDesc();
}
