package com.example.onlineshoppingmall.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemEntity {
    private Long id;
    private String userId;
    private Long productId;
    private Long codeId;
    private int quantity;
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
}
