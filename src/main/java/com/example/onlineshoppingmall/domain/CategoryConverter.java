package com.example.onlineshoppingmall.domain;

import com.example.onlineshoppingmall.dto.CategoryDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryConverter {

    public static CategoryEntity toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setId(categoryDTO.getId());
        entity.setCodeId(categoryDTO.getCodeId());
        entity.setName(categoryDTO.getName());
        entity.setParentId(categoryDTO.getParentId());
        entity.setCreatedUser(categoryDTO.getCreatedUser());
        entity.setUpdatedUser(categoryDTO.getUpdatedUser());
        entity.setCreatedAt(categoryDTO.getCreatedAt());
        entity.setUpdatedAt(categoryDTO.getUpdatedAt());
        entity.setUseYn(categoryDTO.getUseYn());

        return entity;
    }

    public static CategoryDTO toDTO(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(categoryEntity.getId());
        dto.setCodeId(categoryEntity.getCodeId());
        dto.setName(categoryEntity.getName());
        dto.setParentId(categoryEntity.getParentId());
        dto.setCreatedUser(categoryEntity.getCreatedUser());
        dto.setUpdatedUser(categoryEntity.getUpdatedUser());
        dto.setCreatedAt(categoryEntity.getCreatedAt());
        dto.setUpdatedAt(categoryEntity.getUpdatedAt());
        dto.setUseYn(categoryEntity.getUseYn());

        return dto;
    }

    public static List<CategoryDTO> toEntityList(List<CategoryEntity> categoryEntities) {
        if (categoryEntities == null) {
            return Collections.emptyList();
        }

        return categoryEntities.stream()
                .map(CategoryConverter::toDTO)
                .collect(Collectors.toList());
    }
}
