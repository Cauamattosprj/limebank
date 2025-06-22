package com.cauamattosprj.limebank.domains.common.exceptions;

import com.cauamattosprj.limebank.domains.auth.exceptions.EmailAlreadyExistsException;
import com.cauamattosprj.limebank.domains.common.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        ApiResponse<Object> response = ApiResponse.ofError("Erro interno: " + ex.getMessage(), 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ApiResponse<Object> response = ApiResponse.ofError("Email duplicado",409);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}

