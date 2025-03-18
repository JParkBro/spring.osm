package com.example.onlineshoppingmall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Users", timeToLive = 86400)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryMappingEntity {
    @Id
    private Long id;
    private Long productId;
    private Long categoryId;
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
}
