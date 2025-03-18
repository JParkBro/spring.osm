package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class CartItemCountAdvice {

    private final CartService cartService;

    @ModelAttribute("cartItemCount")
    public Integer getCartItemCount(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String userId = authentication.getName();
            return cartService.getCartItemCount(userId);
        }
        return 0;

    }
}
