package com.example.onlineshoppingmall.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderIdGenerator {

    public static final String PAYMENT_CARD = "C";      // 카드
    public static final String PAYMENT_BANK = "B";      // 무통장입금
    public static final String PAYMENT_NAVER = "N";     // 네이버페이
    public static final String PAYMENT_KAKAO = "K";     // 카카오페이
    public static final String PAYMENT_TOSS = "T";      // 토스

    public static String generateOrderId(String paymentMethod, String paymentType) {
        // 형식: 년월일시분초 + 결제방법코드 + '-' + 랜덤문자열

        // 1. 년월일시분초
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());

        // 2. 결제방법코드
        String paymentCode = getPaymentMethodCode(paymentMethod, paymentType);

        // 3. 랜덤 문자열
        String randomString = generateRandomAlphanumeric();

        // 4. 조합
        return timestamp + paymentCode + "-" + randomString;
    }

    private static String getPaymentMethodCode(String paymentMethod, String paymentType) {
        switch (paymentMethod.toLowerCase()) {
            case "card":
                return PAYMENT_CARD;
            case "bank":
                return PAYMENT_BANK;
            case "simple":
                if (paymentType != null) {
                    switch (paymentType.toLowerCase()) {
                        case "naverpay":
                            return PAYMENT_NAVER;
                        case "kakaopay":
                            return PAYMENT_KAKAO;
                        case "tosspay":
                            return PAYMENT_TOSS;
                    }
                }
                return "S";
            default:
                return "X";
        }
    }

    private static String generateRandomAlphanumeric() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }
}
