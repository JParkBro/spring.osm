package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.dto.CategoryDTO;
import com.example.onlineshoppingmall.dto.ProductDeleteDTO;
import com.example.onlineshoppingmall.dto.ProductFormDTO;
import com.example.onlineshoppingmall.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminService adminService;
    private static final Logger logger = LoggerFactory.getLogger(AdminApiController.class);

    @PostMapping("/categories/insert")
    public ResponseEntity<CategoryDTO> insertCategory(@RequestBody CategoryDTO categoryDTO) {
        return adminService.insertCategory(categoryDTO);
    }

    @PatchMapping("/categories/delete")
    public ResponseEntity<String> deleteCategory(@RequestBody CategoryDTO categoryDTO) {
        return adminService.deleteCategory(categoryDTO);
    }

    @PatchMapping("/categories/update")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        return adminService.updateCategory(categoryDTO);
    }

    @PostMapping("/products/insert")
    public ResponseEntity<?> insertProduct(@ModelAttribute ProductFormDTO productFormDTO) {
        try {
            if (productFormDTO.getImages() == null || productFormDTO.getImages().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Images are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            Long productId = adminService.registerCompleteProduct(productFormDTO);

            if (productId != null) {
                Map<String, Object> successResponse = new HashMap<>();
                successResponse.put("success", true);
                successResponse.put("message", "Product successfully registered");
                successResponse.put("productId", productId);
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Failed to register product");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } catch (Exception e) {
            logger.error("Error in product insertion: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PatchMapping("/products/delete")
    public ResponseEntity<String> deleteProduct(@RequestBody ProductDeleteDTO deleteDTO) {
        try {
            System.out.println(deleteDTO.getProductId());
            adminService.deleteProduct(deleteDTO.getProductId());

            return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/products/update")
    public ResponseEntity<?> updateProduct(@ModelAttribute ProductFormDTO productFormDTO) {
        try {
            boolean noNewImages = productFormDTO.getImages() == null || productFormDTO.getImages().isEmpty();
            boolean noExistingImages = productFormDTO.getExistingImageIds() == null || productFormDTO.getExistingImageIds().isEmpty();

            if (noNewImages && noExistingImages) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Images are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            adminService.updateProduct(productFormDTO);
            return ResponseEntity.ok("상품이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            logger.error("상품 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
