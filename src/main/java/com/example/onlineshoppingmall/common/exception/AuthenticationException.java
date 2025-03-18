package com.example.onlineshoppingmall.common.exception;

public class AuthenticationException extends BusinessException {
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_ERROR");
    }
}
