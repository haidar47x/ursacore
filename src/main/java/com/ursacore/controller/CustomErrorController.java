package com.ursacore.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler
    ResponseEntity<?> handleJpaViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        Throwable cause = exception.getCause();
        if (cause != null && cause.getCause() instanceof ConstraintViolationException ve) {
            List<Map<String, String>> errors = ve.getConstraintViolations()
                    .stream()
                    .map(violation -> Map.of(violation.getPropertyPath().toString(), violation.getMessage()))
                    .toList();
            return responseEntity.body(errors);
        }
        return responseEntity.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors()
                .stream()
                .map(err -> Map.of(err.getField(), err.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(errorList);
    }
}
