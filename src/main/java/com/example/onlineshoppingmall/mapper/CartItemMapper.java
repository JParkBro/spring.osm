package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.CartItemEntity;
import com.example.onlineshoppingmall.dto.AddCartDTO;
import com.example.onlineshoppingmall.dto.ProductImageDTO;
import com.example.onlineshoppingmall.dto.ProductInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartItemMapper {

    int insertCartItem(AddCartDTO addCartDTO);

    CartItemEntity findCartItemByUserIdAndProductId(AddCartDTO addCartDTO);

    int updateCartItemQuantity(Long id, int quantity);

    List<CartItemEntity> findCartItemsByUserId(String userId);

    ProductInfoDTO findProductInfoByProductId(Long productId);

    ProductImageDTO findProductImageByProductId(Long productId);

    int deleteCartItem(Long id);

    CartItemEntity findCartItemById(Long id);

    Integer countCartItemsByUserId(String userId);
}
