package com.jxx.xuni.studyproduct;

import com.jxx.xuni.exception.ExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Order(1)
@RestControllerAdvice(basePackages = "com.jxx.xuni.studyproduct.presentation")
public class StudyProductExceptionHandler {

    /**URI에 쿼리 값을 잘못 입력했을 때 발생하는 오류**/
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentTypeMismatchExceptionHandle() {
        ExceptionResponse response = ExceptionResponse.of(400, "정확한 카테고리를 입력해주세요. 카테고리는 대문자 형식입니다.");
        return ResponseEntity.ok().body(response);
    }
}
