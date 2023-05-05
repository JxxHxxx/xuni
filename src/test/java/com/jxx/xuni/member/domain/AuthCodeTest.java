package com.jxx.xuni.member.domain;

import com.jxx.xuni.member.domain.exception.AuthCodeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.jxx.xuni.member.domain.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthCodeTest {

    AuthCode authCode;

    @BeforeEach
    void beforeEach() {
        authCode = new AuthCode("test@naver.com", UsageType.SIGNUP);
    }

    @DisplayName("객체 초기화 시 id, value, isAuthenticated 필드는 다음과 같이 초기화 되어야 한다" +
            "id는 UUID 형식(32 글자)이다." +
            "value는 인증 코드를 의미하며 00자리로 구성된 문자이다" +
            "isAuthenticated는 false 값을 가진다")
    @Test
    void init() {
        String pureUUID = authCode.getAuthCodeId().replace("-", "");
        assertThat(pureUUID.length()).isEqualTo(32);
        assertThat(authCode.getValue().length()).isEqualTo(12);
        assertThat(authCode.isAuthenticated()).isFalse();
    }

    @DisplayName("AuthCode 값과 동일한 값으로 코드 인증을 수행하면 인증은 정상적으로 수행된다" +
            "isAuthenticated 필드 값은 true 로 변경된다.")
    @Test
    void verify_auth_code_success() {
        String authCodeValue = authCode.getValue();
        //when
        authCode.verifyAuthCode(authCodeValue);
        //then
        assertThat(authCode.isAuthenticated()).isTrue();
    }

    @DisplayName("AuthCode 값과 동일하지 않은 값으로 코드 인증을 시도할 경우 " +
            "AuthCodeException 예외가 발생하고 " +
            "예외 메시지 INCORRECT_AUTH_CODE_VALUE" +
            "isAuthenticated 필드 값은 false 에서 변경되지 않는다")
    @Test
    void verify_auth_code_fail_cause_incorrect_value_of_auth_code() {
        //given
        String incorrectAuthCodeValue = "00000000000INCORECT000AUTH000CODE000VALUE00000000000000";
        //when - then
        assertThatThrownBy(() -> authCode.verifyAuthCode(incorrectAuthCodeValue))
                .isInstanceOf(AuthCodeException.class)
                .hasMessage(INCORRECT_AUTH_CODE_VALUE);

        assertThat(authCode.isAuthenticated()).isFalse();
    }

    class TestAuthCode extends AuthCode {
        void modifyValueCreatedTime(LocalDateTime localDateTime) {
            this.valueCreatedTime = localDateTime;
        }

        public TestAuthCode(String email, UsageType usageType) {
            super(email, usageType);
        }
    }

    @DisplayName("AuthCode Value 의 유효 시간이 지나 코드 인증을 시도할 경우" +
            "AuthCodeException 예외 발생" +
            "예외 메시지 VALID_TIME_OUT " +
            "isAuthenticated 필드 값은 false 에서 변경되지 않는다")
    @Test
    void verify_auth_code_fail_cause_valid_time_is_over() {
        TestAuthCode testAuthCode = new TestAuthCode("test@naver.com", UsageType.SIGNUP);
        testAuthCode.modifyValueCreatedTime(LocalDateTime.of(2000,12,31, 12, 00));

        assertThatThrownBy(() -> testAuthCode.verifyAuthCode(testAuthCode.getValue()))
                .isInstanceOf(AuthCodeException.class)
                .hasMessage(VALID_TIME_OUT);

        assertThat(authCode.isAuthenticated()).isFalse();
    }

    @DisplayName("AuthCode 가 인증되었다면 계정(Member) 객체를 생성할 수 있다")
    @Test
    void create_member_success() {
        //given
        String authCodeValue = authCode.getValue();
        authCode.verifyAuthCode(authCodeValue);

        Member member = authCode.createMember("test@naver.com", "0000", "김유니");

        //then
        assertThat(member.receiveEmail()).isEqualTo("test@naver.com");
        assertThat(member.getName()).isEqualTo("김유니");
    }

    @DisplayName("AuthCode 가 인증되지 않은 상태에서 계정 객체를 생성하려는 경우" +
            "AuthCodeException 예외가 발생한다" +
            "예외 메시지 UNAUTHENTICATED")
    @Test
    void create_member_fail_cause_un_authenticated() {
        //given
        AuthCode unAuthenticatedAuthCode = authCode;

        //when - then
        Assertions.assertThatThrownBy(() -> unAuthenticatedAuthCode.createMember("test@naver.com", "0000", "김유니"))
                .isInstanceOf(AuthCodeException.class)
                .hasMessage(UNAUTHENTICATED);
    }
}