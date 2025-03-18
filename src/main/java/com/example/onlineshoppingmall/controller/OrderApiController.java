package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.dto.OrderCompleteDTO;
import com.example.onlineshoppingmall.dto.UserDTO;
import com.example.onlineshoppingmall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping("/complete")
    public ResponseEntity<?> orderComplete(@RequestBody OrderCompleteDTO dto) {
        if (dto.getRecipient() == null || dto.getRecipient().isEmpty()) {
            return ResponseEntity.badRequest().body("수령인 정보가 누락되었습니다.");
        }

        if (!dto.isTermsAgreed() || !dto.isPrivacyAgreed()) {
            return ResponseEntity.badRequest().body("약관 동의가 필요합니다.");
        }

        Map<String, Object> response = new HashMap<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            dto.setUserId(authentication.getName());

            String orderId = orderService.processOrder(dto);

            response.put("orderId", orderId);
            response.put("success", true);
            response.put("message", "주문이 성공적으로 처리되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "주문 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
