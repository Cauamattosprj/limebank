package com.cauamattosprj.limebank.domains.common.exceptions;

import com.cauamattosprj.limebank.domains.auth.exceptions.EmailAlreadyExistsException;
import com.cauamattosprj.limebank.domains.auth.exceptions.InvalidCredentials;
import com.cauamattosprj.limebank.domains.common.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        ApiResponse<Object> response = ApiResponse.ofError("Erro interno: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidCredentials(InvalidCredentials ex) {
        ApiResponse<Object> response = ApiResponse.ofError("Credenciais Inválidas", HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ApiResponse<Object> response = ApiResponse.ofError("Email duplicado",HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}

