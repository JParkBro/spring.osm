package com.example.onlineshoppingmall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    private String orderId;             // 주문번호 (형식: 년월일시분초+결제방법코드-랜덤문자열)
    private String userId;              // 주문자 ID (회원)
    private Long codeId;                // 주문상태 (결제완료, 배송준비중, 배송중, 배송완료, 취소, 환불 등)
    private LocalDateTime orderDate;    // 주문일시
    private int totalProductPrice;      // 상품총액
    private int shippingCost;           // 배송비
    private String recipientName;       // 수령인 이름
    private String phone;               // 수령인 연락처
    private String postalCode;          // 우편번호
    private String address;             // 주소
    private String detailAddress;       // 상세주소
    private String deliveryRequest;     // 배송 요청사항
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
}
