package com.example.onlineshoppingmall.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 요청이 HTML 응답을 기대하는지 확인
     */
    private boolean isHtmlRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains(MediaType.TEXT_HTML_VALUE);
    }

    /**
     * 개발 환경인지 여부 확인 (스택 트레이스 표시 여부 결정)
     */
    private boolean isDevelopmentEnvironment() {
        // 여기서는 간단히 true를 반환하지만, 실제로는 프로파일 설정에 따라 결정
        // 예: return "dev".equals(environment.getActiveProfiles()[0]);
        return true;
    }

    /**
     * @Valid, @Validated 어노테이션으로 검증 실패시 발생하는 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Object handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleMethodArgumentNotValidException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("errorCode", ErrorCode.INVALID_INPUT_VALUE.getCode());
            model.addAttribute("errorMessage", errorMessage);
            return "pages/error/400";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * ModelAttribute 으로 binding error 발생시 BindException 발생
     */
    @ExceptionHandler(BindException.class)
    protected Object handleBindException(
            BindException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleBindException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("errorCode", ErrorCode.INVALID_INPUT_VALUE.getCode());
            model.addAttribute("errorMessage", errorMessage);
            return "pages/error/400";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 실패시 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected Object handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleMethodArgumentTypeMismatchException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.INVALID_INPUT_VALUE.getCode());
            model.addAttribute("errorMessage", "잘못된 형식의 값이 입력되었습니다");
            return "pages/error/400";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 리소스를 찾을 수 없을 때 발생하는 예외 처리
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    protected Object handleResourceNotFoundException(
            ResourceNotFoundException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleResourceNotFoundException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.RESOURCE_NOT_FOUND.getCode());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/error/404";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 입력값 검증 실패시 발생하는 예외 처리
     */
    @ExceptionHandler(ValidationException.class)
    protected Object handleValidationException(
            ValidationException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleValidationException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.INVALID_INPUT_VALUE.getCode());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/error/400";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 인증 관련 예외 처리
     */
    @ExceptionHandler(AuthenticationException.class)
    protected Object handleAuthenticationException(
            AuthenticationException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleAuthenticationException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.UNAUTHORIZED.getCode());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/error/403";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 접근 권한 관련 예외 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected Object handleAccessDeniedException(
            AccessDeniedException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleAccessDeniedException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.ACCESS_DENIED.getCode());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/error/403";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.ACCESS_DENIED, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * 데이터 무결성 위반 예외 처리
     */
    @ExceptionHandler(DataIntegrityException.class)
    protected Object handleDataIntegrityException(
            DataIntegrityException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleDataIntegrityException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.INVALID_INPUT_VALUE.getCode());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/error/400";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * 파일 저장 관련 예외 처리
     */
    @ExceptionHandler(FileStorageException.class)
    protected Object handleFileStorageException(
            FileStorageException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleFileStorageException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.FILE_UPLOAD_ERROR.getCode());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/error/error";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_UPLOAD_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    protected Object handleBusinessException(
            BusinessException e,
            HttpServletRequest request,
            Model model) {

        log.error("handleBusinessException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.INTERNAL_SERVER_ERROR.getCode());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/error/error";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected Object handleException(
            Exception e,
            HttpServletRequest request,
            Model model) {

        log.error("handleException", e);

        // HTML 응답인 경우
        if (isHtmlRequest(request)) {
            model.addAttribute("errorCode", ErrorCode.INTERNAL_SERVER_ERROR.getCode());
            model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");

            // 개발 환경인 경우 스택 트레이스 추가
            if (isDevelopmentEnvironment()) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                model.addAttribute("errorStack", sw.toString());
            }

            return "pages/error/error";
        }

        // API 응답인 경우
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}