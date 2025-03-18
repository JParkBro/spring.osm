package com.example.onlineshoppingmall.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String code;
    private String message;
    private List<FieldError> errors;

    private ErrorResponse(ErrorCode errorCode) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(ErrorCode errorCode, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getCode();
        this.message = message;
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        List<FieldError> fieldErrors = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(error -> {
            fieldErrors.add(new FieldError(
                    error.getField(),
                    error.getRejectedValue() != null ? error.getRejectedValue().toString() : "",
                    error.getDefaultMessage()));
        });

        return new ErrorResponse(errorCode, fieldErrors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}
