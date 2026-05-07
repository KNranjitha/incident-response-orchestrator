package com.internship.tool;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {

        Map<String, String> error = new HashMap<>();

        error.put("message", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> error = new HashMap<>();

        error.put("message", "Validation Failed");

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }
}