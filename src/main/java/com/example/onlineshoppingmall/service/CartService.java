package com.example.onlineshoppingmall.service;

import com.example.onlineshoppingmall.dto.CartItemDTO;
import com.example.onlineshoppingmall.mapper.CartItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartItemMapper cartItemMapper;

    public boolean deleteCartItem(Long productId) {
        int deletedRows = cartItemMapper.deleteCartItem(productId);
        return deletedRows > 0;
    }

    public int updateCartItemQuantity(CartItemDTO cartItemDTO) {
        cartItemMapper.updateCartItemQuantity(cartItemDTO.getId(), cartItemDTO.getQuantity());
        return cartItemDTO.getQuantity();
    }

    public Integer getCartItemCount(String userId) {
        return cartItemMapper.countCartItemsByUserId(userId);
    }

}
