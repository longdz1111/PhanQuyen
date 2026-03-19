package com.poly.project_management.exception;

import com.poly.project_management.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = org.springframework.validation.BindException.class)
    public ResponseEntity<ApiResponse<String>> handlingValidation(org.springframework.validation.BindException exception) {

        // Vì bắt đúng gốc rễ nên IntelliJ sẽ đọc được hàm này xanh mướt!
        String errorMessage = exception.getFieldError().getDefaultMessage();

        ApiResponse<String> apiResponse = new ApiResponse<>(400, errorMessage, null);
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handlingIllegalArgument(IllegalArgumentException exception) {
        ApiResponse<String> apiResponse = new ApiResponse<>(400, exception.getMessage(), null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handlingRuntimeException(RuntimeException exception) {
        ApiResponse<String> apiResponse = new ApiResponse<>(500, "Lỗi hệ thống: " + exception.getMessage(), null);
        return ResponseEntity.internalServerError().body(apiResponse);
    }
}