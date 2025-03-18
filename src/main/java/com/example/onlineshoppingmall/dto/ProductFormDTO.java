package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFormDTO {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer stockQuantity;
    private Long categoryId;
    private List<MultipartFile> images;
    private List<Long> existingImageIds;
    private List<Long> deletedImageIds;
}
