package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompleteDTO {
    private String recipient;           // 수령인
    private String mobilePhone;         // 연락처
    private String postalCode;          // 우편번호
    private String address;             // 주소
    private String detailAddress;       // 상세주소
    private String deliveryRequest;     // 배송요청사항
    private String paymentMethod;       // 결제 방법 (card, bank, simple)  TODO 결제 관련 테이블 추후 생성
    private String simplePaymentType;   // 간편결제 유형 (naverpay, kakaopay, tosspay)
    private int totalProductPrice;      // 상품 총액
    private int shippingCost;           // 배송비
    private boolean termsAgreed;        // 구매 약관 동의 여부
    private boolean privacyAgreed;      // 개인정보 수집 동의 여부

    private List<OrderItemDTO> orderItems; // 주문 상품 목록

    private String userId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long cartId;        // 장바구니 ID (장바구니 담았을 시)
        private Long productId;     // 상품 ID
        private int quantity;       // 수량
        private int price;          // 가격
    }
}
