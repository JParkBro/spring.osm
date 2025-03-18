package com.example.onlineshoppingmall.common.util;

import javax.validation.ValidationException;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\d{3}-\\d{3,4}-\\d{4}$");

    public static void validateEmail(String email) {
        if (email == null || email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || phone.isEmpty() || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Invalid phone number format (000-0000-0000)");
        }
    }

    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.isEmpty()) {
            throw new ValidationException(fieldName + " must not be empty");
        }
    }

    public static void validatePositive(Integer value, String fieldName) {
        if (value == null || value <= 0) {
            throw new ValidationException(fieldName + " must be a positive number");
        }
    }

    public static void validateMinLength(String value, int minLength, String fieldName) {
        if (value == null || value.length() < minLength) {
            throw new ValidationException(fieldName + " must be at least " + minLength + " characters long");
        }
    }

    public static void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new ValidationException(fieldName + " must not exceed " + maxLength + " characters");
        }
    }
}
