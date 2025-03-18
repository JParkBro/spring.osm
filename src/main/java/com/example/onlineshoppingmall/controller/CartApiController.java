package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.dto.CartItemDTO;
import com.example.onlineshoppingmall.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    @PatchMapping("/delete")
    public ResponseEntity<?> deleteCartItem(@RequestParam("id") Long id) {
        try {
            boolean isDeleted = cartService.deleteCartItem(id);

            Map<String, Object> response = new HashMap<>();
            if (isDeleted) {
                response.put("success", true);
                response.put("message", "상품이 장바구니에서 삭제되었습니다.");
                return ResponseEntity.ok().body(response);
            } else {
                response.put("success", false);
                response.put("message", "삭제할 상품을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "상품 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody CartItemDTO cartItemDTO) {
        try {
            // 수량 최소값 검증
            if (cartItemDTO.getQuantity() < 1) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "수량은 1 이상이어야 합니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            int updateQuantity = cartService.updateCartItemQuantity(cartItemDTO);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "수량이 업데이트되었습니다.");

            Map<String, Object> data = new HashMap<>();
            data.put("quantity", updateQuantity);
            successResponse.put("data", data);

            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "수량 업데이트 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String userId = authentication.getName();
            Integer count = cartService.getCartItemCount(userId);
            return ResponseEntity.ok(count);
        }
        return ResponseEntity.ok(0);
    }
}
