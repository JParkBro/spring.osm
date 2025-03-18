package com.example.onlineshoppingmall.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
    private Long id;
    private Long referenceId;
    private String referenceType;
    private String imageUri;
    private int displayOrder;
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
}
