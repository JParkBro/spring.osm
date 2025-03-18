package com.example.onlineshoppingmall.domain;

import com.example.onlineshoppingmall.dto.AuthRegisterDTO;
import com.example.onlineshoppingmall.dto.AuthResponseDTO;
import com.example.onlineshoppingmall.dto.UserDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static UserEntity toEntity(AuthRegisterDTO dto) {
        return UserEntity.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .mobilePhone(dto.getMobilePhone())
                .postalCode(dto.getPostalCode())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .termsAgreed(dto.isTermsAgreed())
                .privacyAgreed(dto.isPrivacyAgreed())
                .build();
    }

    public static AuthResponseDTO toResponseDTO(UserEntity entity) {
        return AuthResponseDTO.builder()
                .userId(entity.getUserId())
                .name(entity.getName())
                .email(entity.getEmail())
                .mobilePhone(entity.getMobilePhone())
                .postalCode(entity.getPostalCode())
                .address(entity.getAddress())
                .detailAddress(entity.getDetailAddress())
                .build();
    }

    public static List<UserDTO> toEntityList(List<UserEntity> userEntities) {
        if (userEntities == null) {
            return Collections.emptyList();
        }

        return userEntities.stream()
                .map(UserConverter::toDTO)
                .collect(Collectors.toList());
    }

    public static UserDTO toDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setUserId(userEntity.getUserId());
        dto.setName(userEntity.getName());
        dto.setEmail(userEntity.getEmail());
        dto.setMobilePhone(userEntity.getMobilePhone());
        dto.setAddress(userEntity.getAddress());
        dto.setDetailAddress(userEntity.getDetailAddress());
        dto.setPostalCode(userEntity.getPostalCode());
        dto.setTermsAgreed(userEntity.isTermsAgreed());
        dto.setPrivacyAgreed(userEntity.isPrivacyAgreed());
        dto.setVerificationKey(userEntity.getVerificationKey());
        dto.setCreatedUser(userEntity.getCreatedUser());
        dto.setCreatedAt(userEntity.getCreatedAt());
        dto.setUpdatedUser(userEntity.getUpdatedUser());
        dto.setUpdatedAt(userEntity.getUpdatedAt());
        dto.setUseYn(userEntity.getUseYn());
        dto.setRole(userEntity.getRole());

        return dto;
    }
}
