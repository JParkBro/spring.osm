package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartDTO {
    private String userId;
    private Long productId;
    private int quantity;
}
