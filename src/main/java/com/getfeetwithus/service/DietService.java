package com.getfeetwithus.service;

import com.getfeetwithus.dto.DietUploadDto;
import com.getfeetwithus.entity.DietMaster;
import com.getfeetwithus.entity.DietUserUpload;
import com.getfeetwithus.entity.User;
import com.getfeetwithus.repository.DietMasterRepository;
import com.getfeetwithus.repository.DietUserUploadRepository;
import com.getfeetwithus.repository.UserRepository;
import com.getfeetwithus.util.FileStorageService;
import com.getfeetwithus.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DietService {

    private final DietMasterRepository dietMasterRepository;
    private final DietUserUploadRepository dietUserUploadRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final IdGeneratorUtil idGeneratorUtil;

    public DietService(DietMasterRepository dietMasterRepository,
                       DietUserUploadRepository dietUserUploadRepository,
                       UserRepository userRepository,
                       FileStorageService fileStorageService,
                       IdGeneratorUtil idGeneratorUtil) {
        this.dietMasterRepository = dietMasterRepository;
        this.dietUserUploadRepository = dietUserUploadRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.idGeneratorUtil = idGeneratorUtil;
    }

    public Optional<DietMaster> getTodayDiet() {
        LocalDate today = LocalDate.now();
        Optional<DietMaster> todayDiet = dietMasterRepository.findFirstByDietDateAndIsActiveTrueOrderByCreatedAtDesc(today);
        if (todayDiet.isPresent()) {
            return todayDiet;
        }
        // Fallback to most recent active diet plan
        return dietMasterRepository.findFirstByIsActiveTrueOrderByDietDateDesc();
    }

    public List<DietMaster> getAllDietPlans() {
        return dietMasterRepository.findAllByOrderByDietDateDesc();
    }

    public Optional<DietMaster> findByDietId(String dietId) {
        return dietMasterRepository.findByDietId(dietId);
    }

    @Transactional
    public DietMaster saveDietPlan(DietMaster diet, MultipartFile imageFile, String adminId) {
        if (diet.getDietId() == null || diet.getDietId().isBlank()) {
            diet.setDietId(idGeneratorUtil.generateDietId());
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.storeFile(imageFile, "diets");
            diet.setImageUrl(imagePath);
        }
        diet.setCreatedBy(adminId);
        if (diet.getDietDate() == null) {
            diet.setDietDate(LocalDate.now());
        }
        return dietMasterRepository.save(diet);
    }

    @Transactional
    public void toggleDietStatus(String dietId) {
        DietMaster diet = dietMasterRepository.findByDietId(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet plan not found: " + dietId));
        diet.setIsActive(!Boolean.TRUE.equals(diet.getIsActive()));
        dietMasterRepository.save(diet);
    }

    @Transactional
    public DietUserUpload saveUserDietUpload(String userId, DietUploadDto dto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        String filePath = null;
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            filePath = fileStorageService.storeFile(dto.getFile(), "user_diets");
        }

        DietUserUpload upload = new DietUserUpload();
        upload.setUserId(userId);
        upload.setUserName(user.getName());
        upload.setDietId(dto.getDietId());
        upload.setDietTitle(dto.getDietTitle());
        upload.setUploadDate(LocalDate.now());
        upload.setFilePath(filePath);
        upload.setNotes(dto.getNotes());
        upload.setUploadTime(LocalDateTime.now());

        return dietUserUploadRepository.save(upload);
    }

    public List<DietUserUpload> getUserUploads(String userId) {
        return dietUserUploadRepository.findByUserIdOrderByUploadTimeDesc(userId);
    }

    public List<DietUserUpload> getAllUserUploads() {
        return dietUserUploadRepository.findAllByOrderByUploadTimeDesc();
    }
}
