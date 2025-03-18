package com.example.onlineshoppingmall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemsEntity {
    private Long id;
    private String orderId;     // 주문번호 (ORDERS 테이블 참조)
    private Long productId;     // 상품 ID (PRODUCTS 테이블 참조)
    private int productPrice;   // 주문 당시 상품 가격 (수량 * 후, 1개 가격 x)
    private int quantity;       // 구매 수량
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
}
