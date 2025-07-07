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
    ResponseEntity handleJpaViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();
        if (exception.getCause().getCause() instanceof ConstraintViolationException ve) {
            List<Map<String, String>> errors = ve.getConstraintViolations()
                    .stream()
                    .map(violation -> {
                        Map<String, String> errMap = new HashMap<>();
                        errMap.put(violation.getPropertyPath().toString(), violation.getMessage());
                        return errMap;
                    })
                    .toList();
            return responseEntity.body(errors);
        }
        return responseEntity.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, String> errMap = new HashMap<>();
                    errMap.put(error.getField(), error.getDefaultMessage());
                    return errMap;
                })
                .toList();
        return ResponseEntity.badRequest().body(errorList);
    }
}
