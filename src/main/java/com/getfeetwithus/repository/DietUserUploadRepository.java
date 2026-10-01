package com.getfeetwithus.repository;

import com.getfeetwithus.entity.DietUserUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietUserUploadRepository extends JpaRepository<DietUserUpload, Long> {
    List<DietUserUpload> findByUserIdOrderByUploadTimeDesc(String userId);
    List<DietUserUpload> findByUserIdAndUploadDate(String userId, LocalDate uploadDate);
    List<DietUserUpload> findAllByOrderByUploadTimeDesc();
}
