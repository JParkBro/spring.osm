package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompleteItemDTO {
    private Long id;
    private String orderId;
    private Long productId;
    private String productName;
    private int price;
    private int quantity;
    private String thumbnail;
}
