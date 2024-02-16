package com.partners.onboard.partneronboardws.exception;

import com.partners.onboard.partneronboardws.model.ApiResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DriverAlreadyExistsException.class})
    public ResponseEntity<ApiResponse> handleDriverAlreadyExistsException(HttpServletRequest request, DriverAlreadyExistsException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder().code(400).uri(request.getRequestURI()).timestamp(new Date()).message(exception.getMessage()).build());

    }

    @ExceptionHandler({DriverStateFailureException.class})
    public ResponseEntity<ApiResponse> handleDriverStateFailureException(HttpServletRequest request, DriverStateFailureException exception) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder().code(500).uri(request.getRequestURI()).timestamp(new Date()).message(exception.getMessage()).build());

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse> handleDriverStateFailureException(HttpServletRequest request, Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder().code(500).uri(request.getRequestURI()).timestamp(new Date()).message(exception.getMessage()).build());

    }
}
