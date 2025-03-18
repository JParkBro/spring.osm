package com.example.onlineshoppingmall.service;

import com.example.onlineshoppingmall.domain.*;
import com.example.onlineshoppingmall.dto.*;
import com.example.onlineshoppingmall.mapper.AdminCategoryMapper;
import com.example.onlineshoppingmall.mapper.AdminProductMapper;
import com.example.onlineshoppingmall.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final AdminUserMapper adminUserMapper;
    private final AdminCategoryMapper adminCategoryMapper;
    private final AdminProductMapper adminProductMapper;

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    private static final String STORAGE_BASE_PATH = "/tmp/files";
    private static final String IMAGE_ACCESS_PATH = "/files";

    @Transactional(readOnly = true)
    public Page<UserDTO> getUsers(Map<String, Object> params) {

        List<UserEntity> userEntities = adminUserMapper.findUsersWithPagination(params);
        int totalCount = adminUserMapper.totalCount(params);

        List<UserDTO> userDTOs = UserConverter.toEntityList(userEntities);
        Pageable pageable = PageRequest.of(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("size").toString()));

        return new PageImpl<>(userDTOs, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {

        List<CategoryEntity> categoryEntities = adminCategoryMapper.getAllCategories();

        return CategoryConverter.toEntityList(categoryEntities);
    }

    @Transactional
    public ResponseEntity<CategoryDTO> insertCategory(CategoryDTO categoryDTO) {

        CategoryEntity entity = CategoryConverter.toEntity(categoryDTO);
        entity.setCreatedUser("admin"); // TODO 관리자 이름 수정

        int result = adminCategoryMapper.insertCategory(entity);

        if (result == 1) {
            CategoryDTO updatedDTO = CategoryConverter.toDTO(entity);
            return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(categoryDTO, HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteCategory(CategoryDTO categoryDTO) {
        CategoryEntity entity = CategoryConverter.toEntity(categoryDTO);
        int result = adminCategoryMapper.deleteCategory(entity);

        if (result > 0) {
            return new ResponseEntity<>("Success Delete", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Fail Delete", HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public ResponseEntity<String> updateCategory(CategoryDTO categoryDTO) {
        CategoryEntity entity = CategoryConverter.toEntity(categoryDTO);
        int result = adminCategoryMapper.updateCategory(entity);

        if (result > 0) {
            return new ResponseEntity<>("Success Update", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Fail Update", HttpStatus.CONFLICT);
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProducts(Map<String, Object> params) {

        List<ProductResponseDTO> products = adminProductMapper.findProductsWithPagination(params);
        setCategoryHierarchyBatch(products);

        int totalCount = adminProductMapper.totalCount(params);

        Pageable pageable = PageRequest.of(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("size").toString()));

        return new PageImpl<>(products, pageable, totalCount);
    }

    private void setCategoryHierarchyBatch(List<ProductResponseDTO> productList) {
        Set<Long> categoryIds = new HashSet<>();

        for (ProductResponseDTO product : productList) {
            if (product.getCategoryId() != null) {
                categoryIds.add(product.getCategoryId());
            }
        }

        if (categoryIds.isEmpty()) {
            return;
        }

        List<CategoryEntity> categories = adminCategoryMapper.findCategoriesByIds(new ArrayList<>(categoryIds));

        Map<Long, CategoryEntity> categoryMap = new HashMap<>();
        for (CategoryEntity category : categories) {
            categoryMap.put(category.getId(), category);

            if (category.getParentId() != null) {
                categoryIds.add(category.getParentId());
            }
        }

        Set<Long> missingIds = new HashSet<>();
        for (Long id : categoryIds) {
            if (!categoryMap.containsKey(id)) {
                missingIds.add(id);
            }
        }

        if (!missingIds.isEmpty()) {
            List<CategoryEntity> additionalCategories = adminCategoryMapper.findCategoriesByIds(new ArrayList<>(missingIds));
            for (CategoryEntity category : additionalCategories) {
                categoryMap.put(category.getId(), category);

                if (category.getParentId() != null && !categoryMap.containsKey(category.getParentId())) {
                    missingIds.add(category.getParentId());
                }
            }

            if (!missingIds.isEmpty()) {
                List<CategoryEntity> secondLevelCategories = adminCategoryMapper.findCategoriesByIds(new ArrayList<>(missingIds));
                for (CategoryEntity category : secondLevelCategories) {
                    categoryMap.put(category.getId(), category);
                }
            }
        }

        for (ProductResponseDTO product : productList) {
            Long categoryId = product.getCategoryId();

            if (categoryId != null && categoryMap.containsKey(categoryId)) {
                // 소분류 설정
                CategoryEntity smallCategory = categoryMap.get(categoryId);
                product.setSmallCategory(CategoryConverter.toDTO(smallCategory));

                // 중분류 설정
                if (smallCategory.getParentId() != null && categoryMap.containsKey(smallCategory.getParentId())) {
                    CategoryEntity mediumCategory = categoryMap.get(smallCategory.getParentId());
                    product.setMediumCategory(CategoryConverter.toDTO(mediumCategory));

                    // 대분류 설정
                    if (mediumCategory.getParentId() != null && categoryMap.containsKey(mediumCategory.getParentId())) {
                        CategoryEntity largeCategory = categoryMap.get(mediumCategory.getParentId());
                        product.setLargeCategory(CategoryConverter.toDTO(largeCategory));
                    }
                }
            }
        }
    }

    @Transactional
    public Long registerCompleteProduct(ProductFormDTO productFormDTO) {
        try {
            // 1. 상품 기본 정보 저장
            Long productId = insertProduct(productFormDTO);

            // 2. 카테고리 매핑 저장
            ProductCategoryMappingEntity entity = new ProductCategoryMappingEntity();
            entity.setProductId(productId);
            entity.setCategoryId(productFormDTO.getCategoryId());
            entity.setCreatedUser("admin");
            int mappingResult = insertProductMapping(entity);

            if (mappingResult == 0) {
                throw new RuntimeException("Product category mapping failed");
            }

            // 3. 이미지 저장 (이미지가 있는 경우)
            if (productFormDTO.getImages() != null && !productFormDTO.getImages().isEmpty()) {
                int imageCount = insertProductImages(productId, productFormDTO.getImages());

                if (imageCount == 0) {
                    throw new RuntimeException("No images were saved");
                }
            }

            return productId;
        } catch (Exception e) {
            logger.error("Error during product registration: {}", e.getMessage(), e);
            throw new RuntimeException("Product registration failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Long insertProduct(ProductFormDTO productFormDTO) {
        ProductEntity productEntity = ProductConverter.toEntity(productFormDTO);
        productEntity.setCreatedUser("admin");
        adminProductMapper.insertProduct(productEntity);

        return productEntity.getId();
    }

    @Transactional
    public int insertProductMapping(ProductCategoryMappingEntity entity) {
        return adminProductMapper.insertProductMapping(entity);
    }

    @Transactional
    public int insertProductImages(Long productId, List<MultipartFile> imageFiles) {
        String productDirPath = STORAGE_BASE_PATH + "/product/p_" + productId;
        logger.info("Creating directory: {}", productDirPath);

        try {
            // 모든 중간 디렉토리를 한 번에 생성하는 보다 강력한 방법
            java.nio.file.Path dirPath = java.nio.file.Paths.get(productDirPath);
            java.nio.file.Files.createDirectories(dirPath);
            logger.info("Directory created using Files.createDirectories: {}", productDirPath);

            // 필요한 경우 권한 설정 시도
            try {
                File dir = dirPath.toFile();
                dir.setReadable(true, false);
                dir.setWritable(true, false);
                dir.setExecutable(true, false);
            } catch (Exception e) {
                logger.warn("Could not set directory permissions: {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Failed to create directory using Files.createDirectories: {}", e.getMessage());

            // 기존 방식도 시도 (fallback)
            File baseDir = new File(STORAGE_BASE_PATH);
            if (!baseDir.exists()) {
                boolean baseDirCreated = baseDir.mkdirs();
                logger.info("Base directory created: {}, result: {}", STORAGE_BASE_PATH, baseDirCreated);
            }

            // product 디렉토리 확인/생성
            File productParentDir = new File(STORAGE_BASE_PATH + "/product");
            if (!productParentDir.exists()) {
                boolean productParentDirCreated = productParentDir.mkdirs();
                logger.info("Product parent directory created: {}, result: {}",
                        productParentDir.getAbsolutePath(), productParentDirCreated);
            }

            // 제품별 디렉토리 생성
            File productDir = new File(productDirPath);
            if (!productDir.exists()) {
                boolean dirCreated = productDir.mkdirs();
                logger.info("Product directory creation result: {}", dirCreated);

                if (!dirCreated) {
                    logger.error("Failed to create directory: {}", productDirPath);
                    logger.error("Parent exists: {}, can write: {}",
                            productDir.getParentFile().exists(),
                            productDir.getParentFile().canWrite());

                    // 마지막 수단: 쉘 명령어 시도
                    try {
                        Process process = Runtime.getRuntime().exec("mkdir -p " + productDirPath);
                        int exitCode = process.waitFor();
                        logger.info("Shell mkdir command exit code: {}", exitCode);

                        if (exitCode == 0) {
                            // 성공했으므로 계속 진행
                            logger.info("Directory created using shell command");
                        } else {
                            throw new RuntimeException("Failed to create directory using shell command: " + exitCode);
                        }
                    } catch (Exception ex) {
                        logger.error("Shell command failed: {}", ex.getMessage());
                        throw new RuntimeException("Failed to create directory for product images");
                    }
                }
            }
        }

        int imageCount = 0;
        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile imageFile = imageFiles.get(i);

            if (imageFile.isEmpty()) continue;

            try {
                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String filename = i + fileExtension;
                String fullFilePath = productDirPath + "/" + filename;

                File destFile = new File(fullFilePath);
                imageFile.transferTo(destFile);

                ImageEntity image = getImageEntity(productId, i, filename);

                adminProductMapper.insertImages(image);
                imageCount++;
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image file", e);
            }
        }

        return imageCount;
    }

    private static ImageEntity getImageEntity(Long productId, int i, String filename) {
        ImageEntity image = new ImageEntity();
        image.setReferenceId(productId);
        image.setReferenceType("PRODUCT");

        image.setImageUri(IMAGE_ACCESS_PATH + "/product/p_" + productId + "/" + filename);
        image.setDisplayOrder(i);
        image.setCreatedUser("admin");
        return image;
    }

    public ProductResponseDTO getProductById(Long productId) {
        ProductEntity entity = adminProductMapper.getProductById(productId);
        ProductResponseDTO productResponseDTO = ProductConverter.toDTO(entity);

        Long smallCategoryId = adminProductMapper.findCategoryIdByProductId(productId);
        productResponseDTO.setCategoryId(smallCategoryId);

        // 소분류(name), 중분류(id)
        CategoryEntity smallCategory = adminCategoryMapper.findCategoryById(smallCategoryId);
        productResponseDTO.setSmallCategory(CategoryConverter.toDTO(smallCategory));

        // 중분류, 대분류(id)
        if (smallCategory.getParentId() != null) {
            CategoryEntity mediumCategory = adminCategoryMapper.findCategoryById(smallCategory.getParentId());
            productResponseDTO.setMediumCategory(CategoryConverter.toDTO(mediumCategory));

            if (mediumCategory.getParentId() != null) {
                // 대분류(name)
                CategoryEntity largeCategory = adminCategoryMapper.findCategoryById(mediumCategory.getParentId());
                productResponseDTO.setLargeCategory(CategoryConverter.toDTO(largeCategory));
            }
        }
        List<ImageEntity> imageEntities = adminProductMapper.findByReferenceIdAndReferenceTypeOrderByDisplayOrder(productId, "PRODUCT");

        List<ProductImageDTO> imageDTOs = imageEntities.stream()
                .map(imageEntity -> {
                    ProductImageDTO dto = new ProductImageDTO();
                    dto.setId(imageEntity.getId());
                    dto.setImageUri(imageEntity.getImageUri());
                    dto.setDisplayOrder(imageEntity.getDisplayOrder());
                    return dto;
                })
                .collect(Collectors.toList());

        productResponseDTO.setImages(imageDTOs);

        return productResponseDTO;
    }

    @Transactional
    public void deleteProduct(Long productId) {
        List<Map<String, Object>> productImages = adminProductMapper.getProductImagesForDelete(productId);
        deletePhysicalImageFiles(productId, productImages);

        adminProductMapper.deleteProduct(productId);
        adminProductMapper.deleteProductImages(productId);
    }

    private void deletePhysicalImageFiles(Long productId, List<Map<String, Object>> productImages) {
        if (productImages == null || productImages.isEmpty()) {
            return;
        }

        String uploadPath = "업로드 경로 설정"; // 실제 파일이 저장된 Docker 볼륨 경로

        for (Map<String, Object> image : productImages) {
            String imageUri = (String) image.get("imageUri");
            if (imageUri != null && !imageUri.isEmpty()) {
                // imageUri는 일반적으로 "/files/product/p_91/0.png" 같은 형태
                // 실제 파일 시스템 경로로 변환
                String filePath = uploadPath + imageUri;

                File file = new File(filePath);
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        // 로그 남기기
                        logger.warn("Failed to delete image file: {}", filePath);
                    }
                }
            }
        }

        // 상품 디렉터리도 삭제
        String productDir = uploadPath + "/files/product/p_" + productId;
        File dir = new File(productDir);
        if (dir.exists() && dir.isDirectory()) {
            // 디렉터리 내 파일이 있을 수 있으므로 모두 삭제
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            dir.delete();
        }
    }

    @Transactional
    public void updateProduct(ProductFormDTO productFormDTO) {
        try {
            ProductEntity productEntity = ProductConverter.toEntity(productFormDTO);
            productEntity.setUpdatedUser("admin");
            adminProductMapper.updateProduct(productEntity);

            ProductCategoryMappingEntity mappingEntity = new ProductCategoryMappingEntity();
            mappingEntity.setProductId(productFormDTO.getId());
            mappingEntity.setCategoryId(productFormDTO.getCategoryId());
            mappingEntity.setCreatedUser("admin");
            int mappingResult = adminProductMapper.updateProductMapping(mappingEntity);

            if (mappingResult == 0) {
                throw new RuntimeException("Product category mapping failed during update");
            }

            // 삭제된 이미지 처리
            if (productFormDTO.getDeletedImageIds() != null && !productFormDTO.getDeletedImageIds().isEmpty()) {
                for (Long imageId : productFormDTO.getDeletedImageIds()) {
                    adminProductMapper.deleteImage(imageId);
                    logger.info("Image with ID {} marked as deleted", imageId);
                }
            }

            // 새 이미지 추가
            if (productFormDTO.getImages() != null && !productFormDTO.getImages().isEmpty()) {
                // 현재 표시 순서 조회 (기존 이미지의 최대 표시 순서 + 1부터 시작)
                int currentMaxOrder = adminProductMapper.getMaxImageOrder(productEntity.getId());
                logger.info("currentMaxOrder :: {}", currentMaxOrder);
                insertProductImagesForUpdate(productEntity.getId(), productFormDTO.getImages(), currentMaxOrder + 1);
            }
        } catch (Exception e) {
            logger.error("Error during product update: {}", e.getMessage(), e);
            throw new RuntimeException("Product update failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void insertProductImagesForUpdate(Long productId, List<MultipartFile> imageFiles, int startOrder) {
        String productDirPath = STORAGE_BASE_PATH + "/product/p_" + productId;

        File productDir = new File(productDirPath);
        if (!productDir.exists()) {
            try {
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get(productDirPath));
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory for product images", e);
            }
        }

        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile imageFile = imageFiles.get(i);

            if (imageFile.isEmpty()) continue;

            try {
                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String filename = (startOrder + i) + "_" + System.currentTimeMillis() + fileExtension;
                String fullFilePath = productDirPath + "/" + filename;

                File destFile = new File(fullFilePath);
                imageFile.transferTo(destFile);

                ImageEntity image = getImageEntity(productId, startOrder + i, filename);

                adminProductMapper.insertImages(image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image file during update", e);
            }
        }

    }
}
