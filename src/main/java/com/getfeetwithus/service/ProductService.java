package com.getfeetwithus.service;

import com.getfeetwithus.entity.ProductMaster;
import com.getfeetwithus.repository.ProductMasterRepository;
import com.getfeetwithus.util.FileStorageService;
import com.getfeetwithus.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductMasterRepository productMasterRepository;
    private final FileStorageService fileStorageService;
    private final IdGeneratorUtil idGeneratorUtil;

    public ProductService(ProductMasterRepository productMasterRepository,
                          FileStorageService fileStorageService,
                          IdGeneratorUtil idGeneratorUtil) {
        this.productMasterRepository = productMasterRepository;
        this.fileStorageService = fileStorageService;
        this.idGeneratorUtil = idGeneratorUtil;
    }

    public List<ProductMaster> getAvailableProducts() {
        return productMasterRepository.findByIsActiveTrue();
    }

    public List<ProductMaster> getAllProducts() {
        return productMasterRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<ProductMaster> findByProductId(String productId) {
        return productMasterRepository.findByProductId(productId);
    }

    @Transactional
    public ProductMaster saveProduct(ProductMaster product, MultipartFile imageFile) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(idGeneratorUtil.generateProductId());
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.storeFile(imageFile, "products");
            product.setImageUrl(imagePath);
        }
        return productMasterRepository.save(product);
    }

    @Transactional
    public void toggleProductStatus(String productId) {
        ProductMaster product = productMasterRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        product.setIsActive(!Boolean.TRUE.equals(product.getIsActive()));
        productMasterRepository.save(product);
    }

    @Transactional
    public void toggleProductAvailability(String productId) {
        ProductMaster product = productMasterRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        product.setIsAvailable(!Boolean.TRUE.equals(product.getIsAvailable()));
        productMasterRepository.save(product);
    }
}
