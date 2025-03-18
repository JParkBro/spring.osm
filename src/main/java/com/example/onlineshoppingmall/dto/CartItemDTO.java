package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;                    // cart item id
    private int quantity;               // 수량
    private ProductInfoDTO productInfo; // productId, thumbnail, price, name
}
