package com.example.onlineshoppingmall.global.constants;

public class FileConstants {
    public static final String STORAGE_BASE_PATH = "/tmp/files";
    public static final String IMAGE_ACCESS_PATH = "/files";
    public static final String PRODUCT_IMAGE_DIR = "product";
    public static final String PRODUCT_IMAGE_PREFIX = "p_";

    // 최대 파일 크기
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    // 허용된 이미지 확장자
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};

    private FileConstants() {
        // 인스턴스화 방지
    }
}
