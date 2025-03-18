package com.example.onlineshoppingmall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Products", timeToLive = 86400)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    private Long id; // ID
    private String name;
    private String description;
    private int price;
    private int stockQuantity;
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
    private Long categoryId;
}
