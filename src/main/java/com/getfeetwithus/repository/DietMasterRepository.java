package com.getfeetwithus.repository;

import com.getfeetwithus.entity.DietMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DietMasterRepository extends JpaRepository<DietMaster, Long> {
    Optional<DietMaster> findByDietId(String dietId);
    List<DietMaster> findByDietDateAndIsActiveTrue(LocalDate dietDate);
    List<DietMaster> findAllByOrderByDietDateDesc();
    List<DietMaster> findByIsActiveTrueOrderByDietDateDesc();
    Optional<DietMaster> findFirstByDietDateAndIsActiveTrueOrderByCreatedAtDesc(LocalDate dietDate);
    Optional<DietMaster> findFirstByIsActiveTrueOrderByDietDateDesc();
}
