package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.ProductEntity;
import com.example.onlineshoppingmall.dto.ProductImageDTO;
import com.example.onlineshoppingmall.dto.ProductInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<ProductEntity> getProducts(Long categoryId);

    ProductImageDTO getProductImageDTO(Long productId);

    ProductEntity getProduct(Long productId);

    List<ProductImageDTO> getProductImages(Long productId);
}
