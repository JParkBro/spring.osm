package com.example.onlineshoppingmall.global.constants;

public class OrderConstants {
    // 주문 상태 코드
    public static final String ORDER_STATUS_PAYMENT_COMPLETED = "PAYMENT_COMPLETED";
    public static final String ORDER_STATUS_PREPARING = "PREPARING";
    public static final String ORDER_STATUS_SHIPPING = "SHIPPING";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_CANCELED = "CANCELED";

    // 배송 관련 상수
    public static final int FREE_SHIPPING_THRESHOLD = 50000;
    public static final int STANDARD_SHIPPING_COST = 2500;

    // 결제 방법 코드
    public static final String PAYMENT_METHOD_CARD = "card";
    public static final String PAYMENT_METHOD_BANK = "bank";
    public static final String PAYMENT_METHOD_SIMPLE = "simple";

    // 간편 결제 유형 코드
    public static final String SIMPLE_PAYMENT_TYPE_NAVER = "naverpay";
    public static final String SIMPLE_PAYMENT_TYPE_KAKAO = "kakaopay";
    public static final String SIMPLE_PAYMENT_TYPE_TOSS = "tosspay";

    private OrderConstants() {
        // 인스턴스화 방지
    }
}
