package com.example.onlineshoppingmall.domain;

import com.example.onlineshoppingmall.dto.ProductFormDTO;
import com.example.onlineshoppingmall.dto.ProductInfoDTO;
import com.example.onlineshoppingmall.dto.ProductResponseDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductConverter {
    public static List<ProductInfoDTO> toInfoList(List<ProductEntity> entityList) {
        if (entityList == null) {
            return Collections.emptyList();
        }

        return entityList.stream()
                .map(ProductConverter::toInfoDTO)
                .collect(Collectors.toList());
    }

    public static ProductInfoDTO toInfoDTO(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        ProductInfoDTO dto = new ProductInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStockQuantity(entity.getStockQuantity());
        return dto;
    }

    public static ProductResponseDTO toDTO(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setCategoryId(entity.getCategoryId());

        return dto;
    }

    public static ProductEntity toEntity(ProductFormDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductEntity entity = new ProductEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStockQuantity(dto.getStockQuantity());

        return entity;
    }
}
