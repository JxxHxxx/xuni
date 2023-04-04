package com.jxx.xuni.exception;

import com.jxx.xuni.auth.config.UnauthenticatedException;
import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.NotAppropriateGroupStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CapacityOutOfBoundException.class, NotAppropriateGroupStatusException.class})
    public ResponseEntity<ExceptionResponse> groupCreateExceptionsHandler(RuntimeException exception) {
        ExceptionResponse response = ExceptionResponse.of(400, exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({UnauthenticatedException.class})
    public ResponseEntity<ExceptionResponse> unAuthenticatedExceptionHandler(RuntimeException exception) {
        ExceptionResponse response = ExceptionResponse.of(401, exception.getMessage());
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }
}
