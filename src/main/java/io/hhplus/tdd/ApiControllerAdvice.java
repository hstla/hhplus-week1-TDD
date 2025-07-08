package io.hhplus.tdd;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        String message = (e.getMessage() != null  && !e.getMessage().isBlank()) ? e.getMessage() : "에러가 발생했습니다.";
        return ResponseEntity.status(500).body(new ErrorResponse("500", message));
    }
}