package com.getfeetwithus.service;

import com.getfeetwithus.entity.OrganizationDetails;
import com.getfeetwithus.repository.OrganizationDetailsRepository;
import com.getfeetwithus.util.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class OrganizationService {

    private final OrganizationDetailsRepository organizationDetailsRepository;
    private final FileStorageService fileStorageService;

    public OrganizationService(OrganizationDetailsRepository organizationDetailsRepository,
                               FileStorageService fileStorageService) {
        this.organizationDetailsRepository = organizationDetailsRepository;
        this.fileStorageService = fileStorageService;
    }

    public OrganizationDetails getOrganizationDetails() {
        return organizationDetailsRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    OrganizationDetails org = new OrganizationDetails();
                    org.setOrgName("Get Feet With Us");
                    org.setAboutInfo("Get Feet With Us is a premier wellness and health community platform dedicated to daily diet guidance, fitness/yoga routines, and natural wellness products.");
                    org.setAddress("100 Fitness Boulevard, Health District, Bhubaneswar, Odisha");
                    org.setMobileNumber("+91 9876543210");
                    org.setEmail("support@getfeetwithus.com");
                    org.setWebsite("https://www.getfeetwithus.com");
                    org.setUpdatedAt(LocalDateTime.now());
                    return organizationDetailsRepository.save(org);
                });
    }

    @Transactional
    public OrganizationDetails updateOrganizationDetails(OrganizationDetails details, MultipartFile logoFile) {
        OrganizationDetails existing = getOrganizationDetails();
        existing.setOrgName(details.getOrgName());
        existing.setAboutInfo(details.getAboutInfo());
        existing.setAddress(details.getAddress());
        existing.setMobileNumber(details.getMobileNumber());
        existing.setEmail(details.getEmail());
        existing.setWebsite(details.getWebsite());
        existing.setOtherInfo(details.getOtherInfo());
        existing.setUpdatedAt(LocalDateTime.now());

        if (logoFile != null && !logoFile.isEmpty()) {
            String logoPath = fileStorageService.storeFile(logoFile, "org");
            existing.setLogoUrl(logoPath);
        }

        return organizationDetailsRepository.save(existing);
    }
}
