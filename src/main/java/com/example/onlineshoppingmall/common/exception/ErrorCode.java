package com.example.onlineshoppingmall.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid input value"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "Internal server error"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "Resource not found"),

    // 인증 관련 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "Unauthorized access"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A002", "Access denied"),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "A003", "Invalid login credentials"),

    // 사용자 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "User not found"),
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "U002", "User ID already exists"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "Invalid password"),

    // 상품 관련 에러
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "Product not found"),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "P002", "Product out of stock"),

    // 주문 관련 에러
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O001", "Order not found"),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "O002", "Invalid order status"),
    PAYMENT_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "O003", "Payment processing failed"),

    // 파일 관련 에러
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "File upload failed"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "F002", "Invalid file type"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F003", "File not found");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
