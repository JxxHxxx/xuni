package com.xuni.api.auth.dto.request;

import com.xuni.api.auth.presentation.AuthControllerTestConfig;
import com.xuni.api.support.JwtTestConfiguration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SpringBootTest
@Import({AuthControllerTestConfig.class, JwtTestConfiguration.class})
class SignupFormTest {

    @Autowired
    Validator validator;

    @DisplayName("회원 가입 폼을 검증한다. 각 필드의 제약 조건을 준수하지 않을 시 이에 알맞는 예외 메시지를 응답한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "NA, 1234, 유니, 알 수 없는 에러가 발생했습니다.",
            "3e3402e3-77d1-4405-8b16-f5dfe2041f7d, 123, 유니, '비밀번호는 4 ~ 12자 사이의 소문자, 대문자, 숫자로 이루어져야 합니다.'",
            "3e3402e3-77d1-4405-8b16-f5dfe2041f7d, 1234, 유, 이름은 2 ~ 8자 사이의 한글이어야 합니다."
    }, nullValues = "NA")
    void validate_parameter(String authCodeId, String password, String name, String message) {

        SignupForm signupForm = new SignupForm(authCodeId, password, name);
        Set<ConstraintViolation<SignupForm>> validate = validator.validate(signupForm);

        Iterator<ConstraintViolation<SignupForm>> iterators = validate.iterator();
        List<String> messages = new ArrayList<>();

        while (iterators.hasNext()) {
            ConstraintViolation<SignupForm> next = iterators.next();
            messages.add(next.getMessage());
        }

        Assertions.assertThat(messages).contains(message);
    }

    @DisplayName("회원 가입 폼을 검증한다. 각 필드의 제약 조건을 준수하지 않을 시 이에 알맞는 예외 메시지를 응답한다.")
    @ParameterizedTest
    @CsvSource(value = {"NA, 123, 유"}, nullValues = "NA")
    void validate_parameter_case_violate_one_more(String authCodeId, String password, String name) {

        SignupForm signupForm = new SignupForm(authCodeId, password, name);
        Set<ConstraintViolation<SignupForm>> validate = validator.validate(signupForm);

        Iterator<ConstraintViolation<SignupForm>> iterators = validate.iterator();
        List<String> messages = new ArrayList<>();

        while (iterators.hasNext()) {
            ConstraintViolation<SignupForm> next = iterators.next();
            messages.add(next.getMessage());
        }

        Assertions.assertThat(messages).contains(
                "알 수 없는 에러가 발생했습니다.",
                "비밀번호는 4 ~ 12자 사이의 소문자, 대문자, 숫자로 이루어져야 합니다.",
                "이름은 2 ~ 8자 사이의 한글이어야 합니다.");
    }
}