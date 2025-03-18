package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.dto.AddCartDTO;
import com.example.onlineshoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    @PostMapping("/cart/insert")
    public ResponseEntity<?> insertCartItem(@RequestBody AddCartDTO addCartDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        addCartDTO.setUserId(userId);

        return productService.insertCartItem(addCartDTO);
    }
}
