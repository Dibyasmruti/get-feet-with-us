package com.getfeetwithus.repository;

import com.getfeetwithus.entity.FitnessCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessCompletionRepository extends JpaRepository<FitnessCompletion, Long> {
    Optional<FitnessCompletion> findByUserIdAndFitnessIdAndCompletionDate(String userId, String fitnessId, LocalDate completionDate);
    List<FitnessCompletion> findByUserIdAndCompletionDate(String userId, LocalDate completionDate);
    List<FitnessCompletion> findByUserIdOrderByCompletionDateDescCompletionTimeDesc(String userId);
    List<FitnessCompletion> findAllByOrderByCompletionDateDescCompletionTimeDesc();
    boolean existsByUserIdAndFitnessIdAndCompletionDate(String userId, String fitnessId, LocalDate completionDate);
}
