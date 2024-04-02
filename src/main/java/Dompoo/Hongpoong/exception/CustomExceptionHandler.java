package Dompoo.Hongpoong.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ErrorResponse> handlerException(MyException e, HttpServletRequest request) {

        log.error("errorCode: {} , url: {} , message: {}", e.statusCode(), request.getRequestURI(), e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .code(e.statusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(Integer.parseInt(e.statusCode()))
                .body(response);

    }
}
