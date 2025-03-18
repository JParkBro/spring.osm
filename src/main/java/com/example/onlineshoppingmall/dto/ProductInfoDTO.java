package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDTO {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int stockQuantity;
    private ProductImageDTO thumbnail;
    private List<ProductImageDTO> images;
}
