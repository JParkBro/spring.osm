package com.example.onlineshoppingmall.common.exception;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(String message) {
        super(message, "ACCESS_DENIED");
    }
}
