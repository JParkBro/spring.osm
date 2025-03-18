package com.example.onlineshoppingmall.common.exception;

public class DataIntegrityException extends BusinessException {
    public DataIntegrityException(String message) {
        super(message, "DATA_INTEGRITY_ERROR");
    }

    public DataIntegrityException(String message, Throwable cause) {
        super(message, "DATA_INTEGRITY_ERROR", cause);
    }
}
