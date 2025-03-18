package com.example.onlineshoppingmall.service;

import com.example.onlineshoppingmall.domain.CartItemEntity;
import com.example.onlineshoppingmall.domain.ProductConverter;
import com.example.onlineshoppingmall.domain.ProductEntity;
import com.example.onlineshoppingmall.dto.AddCartDTO;
import com.example.onlineshoppingmall.dto.ProductImageDTO;
import com.example.onlineshoppingmall.dto.ProductInfoDTO;
import com.example.onlineshoppingmall.mapper.CartItemMapper;
import com.example.onlineshoppingmall.mapper.CategoryMapper;
import com.example.onlineshoppingmall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final CartItemMapper cartItemMapper;

    public List<ProductInfoDTO> getProducts(Long categoryId) {
        List<ProductEntity> products = productMapper.getProducts(categoryId);

        List<ProductInfoDTO> infoDTOs = ProductConverter.toInfoList(products);
        for (ProductInfoDTO infoDTO : infoDTOs) {
            ProductImageDTO imageDTO = productMapper.getProductImageDTO(infoDTO.getId());
            infoDTO.setThumbnail(imageDTO);
        }

        return infoDTOs;
    }

    public ProductInfoDTO getProduct(Long id) {

        ProductEntity entity = productMapper.getProduct(id);
        ProductInfoDTO dto = ProductConverter.toInfoDTO(entity);
        List<ProductImageDTO> images = productMapper.getProductImages(dto.getId());
        dto.setImages(images);

        return dto;
    }

    public Map<String, Long> getCategoryHierarchy(Long categoryId) {
        Map<String, Long> hierarchy = new HashMap<>();
        hierarchy.put("level3", categoryId);
        hierarchy.put("level2", categoryMapper.findById(categoryId).getParentId());
        hierarchy.put("level1", categoryMapper.findById(categoryMapper.findById(categoryId).getParentId()).getParentId());
        return hierarchy;
    }

    public ResponseEntity<?> insertCartItem(AddCartDTO addCartDTO) {
        try {
            CartItemEntity existingItem = cartItemMapper.findCartItemByUserIdAndProductId(addCartDTO);

            int result;
            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + addCartDTO.getQuantity();
                result = cartItemMapper.updateCartItemQuantity(existingItem.getId(), newQuantity);
            } else {
                result = cartItemMapper.insertCartItem(addCartDTO);
            }

            Map<String, Object> response = new HashMap<>();
            if (result > 0) {
                response.put("success", true);
                response.put("message", "장바구니에 상품이 추가되었습니다.");

                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "장바구니에 상품을 추가하지 못했습니다.");

                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "장바구니 추가 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
