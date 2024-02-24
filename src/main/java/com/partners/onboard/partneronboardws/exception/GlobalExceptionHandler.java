package com.partners.onboard.partneronboardws.exception;

import com.partners.onboard.partneronboardws.records.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({DriverAlreadyExistsException.class})
    public ResponseEntity<ApiResponse> handleDriverAlreadyExistsException(HttpServletRequest request, DriverAlreadyExistsException exception) {

        log.error("Exception occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder().code(400).uri(request.getRequestURI()).timestamp(new Date()).message(exception.getMessage()).build());

    }

    @ExceptionHandler({DriverNotFoundException.class})
    public ResponseEntity<ApiResponse> handleDriverNotFoundException(HttpServletRequest request, DriverNotFoundException exception) {

        log.error("Exception occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder().code(400).uri(request.getRequestURI()).timestamp(new Date()).message(exception.getMessage()).build());

    }

    @ExceptionHandler({VerifyLinkExpiredException.class})
    public ResponseEntity<ApiResponse> handleVerifyLinkExpiredException(HttpServletRequest request, VerifyLinkExpiredException exception) {

        log.error("Exception occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder().code(400).uri(request.getRequestURI()).timestamp(new Date()).message(exception.getMessage()).build());

    }


    @ExceptionHandler({DriverStateFailureException.class})
    public ResponseEntity<ApiResponse> handleDriverStateFailureException(HttpServletRequest request, DriverStateFailureException exception) {

        log.error("Exception occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder().code(500).uri(request.getRequestURI()).timestamp(new Date()).message(exception.getMessage()).build());

    }
}
