package com.jxx.xuni.exception;

import com.jxx.xuni.auth.domain.exception.UnauthenticatedException;
import com.jxx.xuni.common.exception.NotPermissionException;
import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.GroupJoinException;
import com.jxx.xuni.group.domain.exception.NotAppropriateGroupStatusException;
import com.jxx.xuni.auth.domain.PasswordNotMatchedException;
import com.jxx.xuni.auth.domain.exception.AuthCodeException;
import com.jxx.xuni.statistics.domain.exception.RatingOutOfBoundException;
import com.jxx.xuni.statistics.domain.exception.ReviewCntOutOfBoundException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.REQUIRED_LOGIN;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CapacityOutOfBoundException.class, NotAppropriateGroupStatusException.class, GroupJoinException.class,
            IllegalArgumentException.class, AuthCodeException.class, PasswordNotMatchedException.class, RatingOutOfBoundException.class
    , ReviewCntOutOfBoundException.class})
    public ResponseEntity<ExceptionResponse> domainRoleViolateExceptionHandle(RuntimeException exception) {
        ExceptionResponse response = ExceptionResponse.of(400, exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponses> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> errorMessages = fieldErrors.stream().map(fe -> fe.getDefaultMessage()).toList();

        ExceptionResponses response = ExceptionResponses.of(400, errorMessages);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ExceptionResponse> unAuthenticatedExceptionHandle(RuntimeException exception) {
        ExceptionResponse response = ExceptionResponse.of(401, exception.getMessage());
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ExceptionResponse> malformedJwtExceptionHandle() {
        ExceptionResponse response = ExceptionResponse.of(401, REQUIRED_LOGIN);
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<ExceptionResponse> securityExceptionHandle(SecurityException exception) {
        ExceptionResponse response = ExceptionResponse.of(400, exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler({NotPermissionException.class})
    public ResponseEntity<ExceptionResponse> notPermissionExceptionHandler(RuntimeException exception) {
        ExceptionResponse response = ExceptionResponse.of(403, exception.getMessage());
        return new ResponseEntity<>(response, FORBIDDEN);
    }

}
