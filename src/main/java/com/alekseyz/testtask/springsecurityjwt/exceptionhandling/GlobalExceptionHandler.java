package com.alekseyz.testtask.springsecurityjwt.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleAllException(Exception e) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Произошла ошибка, идентификатор ошибки: " + logError(e));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiError> handleException(TokenException e){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST, e.getMessage(), "Произошла ошибка, идентификатор ошибки: " + logError(e));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiError> handleException(UserException e){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST, e.getMessage(), "Произошла ошибка, идентификатор ошибки: " + logError(e));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleException(AccessDeniedException e) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST, e.getMessage(), "Произошла ошибка, идентификатор ошибки: " + logError(e));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    private String logError(Exception e) {
        String stringUUID = UUID.randomUUID().toString();
        log.error("Ошибка: {} , причина : {} , идентификатор ошибки : {}", e.getMessage(), e.getCause(), stringUUID);
        return stringUUID;
    }




}
