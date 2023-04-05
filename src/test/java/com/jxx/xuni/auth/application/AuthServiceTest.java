package com.jxx.xuni.auth.application;

import com.jxx.xuni.auth.dto.request.LoginForm;
import com.jxx.xuni.auth.dto.request.SignupForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.EXISTED_EMAIL;
import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.LOGIN_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;
    SignupForm signupForm;

    @BeforeEach
    void beforeEach() { // given
        signupForm = new SignupForm("leesin5498@naver.com", "1234", "재헌");
    }

    @DisplayName("회원가입 성공 케이스")
    @Test
    void signup_success() {
        // when - then
        Assertions.assertThatCode(() -> authService.signup(signupForm))
                .doesNotThrowAnyException();;
    }

    @DisplayName("회원가입 실패 IllegalArgumentException 예외, {EXISTED_EMAIL} 메시지 반환")
    @Test
    void signup_fail() {
        //given
        authService.signup(new SignupForm("leesin5498@naver.com", "1235", "유니"));

        //when - then
        assertThatThrownBy(() -> authService.signup(signupForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EXISTED_EMAIL);
    }

    @DisplayName("로그인 성공 시 사용자의 Name, Email, UserId 을 반환한다.")
    @Test
    void login_success() {
        // given
        authService.signup(signupForm);
        LoginForm loginForm = new LoginForm("leesin5498@naver.com", "1234");
        // when
        SimpleMemberDetails memberDetails = authService.login(loginForm);
        // then
        assertThat(memberDetails.getName()).isEqualTo("재헌");
        assertThat(memberDetails.getEmail()).isEqualTo("leesin5498@naver.com");
        assertThat(memberDetails.getUserId()).isNotNull();
    }

    @DisplayName("로그인 실패 케이스")
    @ParameterizedTest(name ="[{index}] : {2}")
    @CsvSource(value = {"leesin5498@naver.com, 1235, 비밀번호가 틀린 경우 ",
                        "jsmtmt@naver.com, 1234, 이메일이 틀린 경우"})
    void login_fail(String email, String password, String message) {
        // given
        authService.signup(signupForm);
        LoginForm loginForm = new LoginForm(email, password);
        // when - then
        Assertions.assertThatThrownBy(() -> authService.login(loginForm))
                .hasMessage(LOGIN_FAIL);

    }
}