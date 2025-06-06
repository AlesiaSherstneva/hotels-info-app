package com.example.hotelsinfoapp.controller.advice;

import com.example.hotelsinfoapp.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.ZoneId;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                         WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> String.format("%s - %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        ErrorResponseDto response = prepareResponse(message, HttpStatus.BAD_REQUEST.value(), request);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(Exception ex, WebRequest request) {
        ErrorResponseDto response = prepareResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleArgumentException(Exception ex, WebRequest request) {
        ErrorResponseDto response = prepareResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), request);

        return ResponseEntity.badRequest().body(response);
    }

    private ErrorResponseDto prepareResponse(String errorMessage, int status, WebRequest request) {
        return ErrorResponseDto.builder()
                .message(errorMessage)
                .status(status)
                .path(request.getDescription(false))
                .timestamp(Instant.now().atZone(ZoneId.systemDefault()))
                .build();
    }
}