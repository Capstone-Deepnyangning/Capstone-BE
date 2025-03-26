package com.deepnyangning.capstonebe.global.exception;

import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected final ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("Exception 발생: {}", e.getErrorCode().getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
                .code(e.getErrorCode().getHttpStatus().value())
                .error(e.getErrorCode().getHttpStatus().name())
                .message(e.getErrorCode().getMessage())
                .build();

        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleGeneralException(Exception e){
        log.error("Exception 발생: {}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
                .error(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().name())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();

        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(response);
    }
}
