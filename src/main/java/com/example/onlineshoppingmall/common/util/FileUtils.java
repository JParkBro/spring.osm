package com.example.onlineshoppingmall.common.util;

import com.example.onlineshoppingmall.common.exception.FileStorageException;
import com.example.onlineshoppingmall.global.constants.FileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
public class FileUtils {
    /**
     * 파일 확장자가 허용된 이미지 확장자인지 확인합니다.
     *
     * @param filename 파일명
     * @return 허용된 이미지 확장자이면 true, 그렇지 않으면 false
     */
    public static boolean isAllowedImageExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        String extension = getExtension(filename).toLowerCase();
        return Arrays.asList(FileConstants.ALLOWED_IMAGE_EXTENSIONS).contains("." + extension);
    }

    /**
     * 파일명에서 확장자를 추출합니다.
     *
     * @param filename 파일명
     * @return 파일 확장자 (점 제외)
     */
    public static String getExtension(String filename) {
        if (filename == null || filename.isEmpty() || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * 임시 파일명을 생성합니다.
     *
     * @param originalFilename 원본 파일명
     * @return UUID + 원본 파일 확장자로 구성된 임시 파일명
     */
    public static String generateTempFilename(String originalFilename) {
        String extension = getExtension(originalFilename);
        return UUID.randomUUID().toString() + (extension.isEmpty() ? "" : "." + extension);
    }

    /**
     * 디렉토리가 존재하지 않으면 생성합니다.
     *
     * @param directory 생성할 디렉토리 경로
     * @throws IOException 디렉토리 생성 중 오류 발생 시
     */
    public static void createDirectoryIfNotExists(String directory) throws IOException {
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Created directory: {}", directory);
        }
    }

    /**
     * 파일이 존재하면 삭제합니다.
     *
     * @param filePath 삭제할 파일 경로
     * @return 파일이 성공적으로 삭제되었으면 true, 그렇지 않으면 false
     */
    public static boolean deleteIfExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                log.info("Deleted file: {}", filePath);
            } else {
                log.warn("Failed to delete file: {}", filePath);
            }
            return deleted;
        }

        return false;
    }

    /**
     * 상품 이미지 경로를 생성합니다.
     *
     * @param productId 상품 ID
     * @return 상품 이미지 디렉토리 경로
     */
    public static String getProductImageDirectory(Long productId) {
        return FileConstants.STORAGE_BASE_PATH + "/" + FileConstants.PRODUCT_IMAGE_DIR + "/" +
                FileConstants.PRODUCT_IMAGE_PREFIX + productId;
    }

    /**
     * 접근 경로를a 실제 파일 시스템 경로로 변환합니다.
     *
     * @param accessPath 접근 경로 (예: "/files/product/image.jpg")
     * @return 실제 파일 시스템 경로 (예: "/tmp/files/product/image.jpg")
     */
    public static String convertToFilePath(String accessPath) {
        if (accessPath == null || accessPath.isEmpty()) {
            return null;
        }

        return accessPath.replace(FileConstants.IMAGE_ACCESS_PATH, FileConstants.STORAGE_BASE_PATH);
    }

    /**
     * 실제 파일 시스템 경로를 접근 경로로 변환합니다.
     *
     * @param filePath 실제 파일 시스템 경로 (예: "/tmp/files/product/image.jpg")
     * @return 접근 경로 (예: "/files/product/image.jpg")
     */
    public static String convertToAccessPath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        return filePath.replace(FileConstants.STORAGE_BASE_PATH, FileConstants.IMAGE_ACCESS_PATH);
    }

    /**
     * 파일을 저장합니다.
     *
     * @param file      저장할 파일
     * @param directory 저장 디렉토리
     * @param filename  저장할 파일명
     * @return 접근 경로
     * @throws FileStorageException 파일 저장 중 오류 발생 시
     */
    public static String saveFile(MultipartFile file, String directory, String filename) throws FileStorageException {
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Failed to store empty file");
            }

            createDirectoryIfNotExists(directory);

            Path destinationPath = Paths.get(directory, filename);
            file.transferTo(destinationPath.toFile());

            log.info("Saved file: {}", destinationPath);

            return FileConstants.IMAGE_ACCESS_PATH + "/" +
                    directory.substring(directory.indexOf(FileConstants.STORAGE_BASE_PATH) +
                            FileConstants.STORAGE_BASE_PATH.length() + 1) +
                    "/" + filename;
        } catch (IOException e) {
            log.error("Failed to save file", e);
            throw new FileStorageException("Failed to save file: " + e.getMessage(), e);
        }
    }
}
