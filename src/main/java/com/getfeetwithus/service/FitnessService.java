package com.getfeetwithus.service;

import com.getfeetwithus.entity.FitnessCompletion;
import com.getfeetwithus.entity.FitnessMaster;
import com.getfeetwithus.entity.User;
import com.getfeetwithus.repository.FitnessCompletionRepository;
import com.getfeetwithus.repository.FitnessMasterRepository;
import com.getfeetwithus.repository.UserRepository;
import com.getfeetwithus.util.FileStorageService;
import com.getfeetwithus.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FitnessService {

    private final FitnessMasterRepository fitnessMasterRepository;
    private final FitnessCompletionRepository fitnessCompletionRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final IdGeneratorUtil idGeneratorUtil;

    public FitnessService(FitnessMasterRepository fitnessMasterRepository,
                          FitnessCompletionRepository fitnessCompletionRepository,
                          UserRepository userRepository,
                          FileStorageService fileStorageService,
                          IdGeneratorUtil idGeneratorUtil) {
        this.fitnessMasterRepository = fitnessMasterRepository;
        this.fitnessCompletionRepository = fitnessCompletionRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.idGeneratorUtil = idGeneratorUtil;
    }

    public List<FitnessMaster> getActiveActivities() {
        return fitnessMasterRepository.findByIsActiveTrue();
    }

    public List<FitnessMaster> getAllActivities() {
        return fitnessMasterRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<FitnessMaster> findByFitnessId(String fitnessId) {
        return fitnessMasterRepository.findByFitnessId(fitnessId);
    }

    @Transactional
    public FitnessMaster saveActivity(FitnessMaster activity, MultipartFile imageFile) {
        if (activity.getFitnessId() == null || activity.getFitnessId().isBlank()) {
            activity.setFitnessId(idGeneratorUtil.generateFitnessId());
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.storeFile(imageFile, "fitness");
            activity.setImageUrl(imagePath);
        }
        return fitnessMasterRepository.save(activity);
    }

    @Transactional
    public void toggleActivityStatus(String fitnessId) {
        FitnessMaster act = fitnessMasterRepository.findByFitnessId(fitnessId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + fitnessId));
        act.setIsActive(!Boolean.TRUE.equals(act.getIsActive()));
        fitnessMasterRepository.save(act);
    }

    @Transactional
    public boolean markAsComplete(String userId, String fitnessId) {
        LocalDate today = LocalDate.now();

        // Prevent duplicate completion records for the same user and applicable date
        if (fitnessCompletionRepository.existsByUserIdAndFitnessIdAndCompletionDate(userId, fitnessId, today)) {
            return false; // Already completed today
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        FitnessMaster act = fitnessMasterRepository.findByFitnessId(fitnessId)
                .orElseThrow(() -> new IllegalArgumentException("Fitness activity not found: " + fitnessId));

        FitnessCompletion completion = new FitnessCompletion();
        completion.setUserId(userId);
        completion.setUserName(user.getName());
        completion.setFitnessId(fitnessId);
        completion.setFitnessTitle(act.getTitle());
        completion.setCompletionDate(today);
        completion.setCompletionTime(LocalTime.now());
        completion.setCompletionStatus("COMPLETED");
        completion.setCreatedAt(LocalDateTime.now());

        fitnessCompletionRepository.save(completion);
        return true;
    }

    public Set<String> getTodayCompletedFitnessIds(String userId) {
        LocalDate today = LocalDate.now();
        return fitnessCompletionRepository.findByUserIdAndCompletionDate(userId, today)
                .stream()
                .map(FitnessCompletion::getFitnessId)
                .collect(Collectors.toSet());
    }

    public List<FitnessCompletion> getUserCompletions(String userId) {
        return fitnessCompletionRepository.findByUserIdOrderByCompletionDateDescCompletionTimeDesc(userId);
    }

    public List<FitnessCompletion> getAllCompletions() {
        return fitnessCompletionRepository.findAllByOrderByCompletionDateDescCompletionTimeDesc();
    }
}
