package com.jxx.xuni.auth.support;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


@ServiceTest
class JwtTokenManagerTest {

    @Autowired
    JwtTokenManager jwtTokenManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    String testToken;

    @BeforeEach // class given
    void beforeEach() {
        MemberDetails memberDetails = new SimpleMemberDetails(123l, "leesin5498@naver.com", "재헌");
        String BearerToken = jwtTokenProvider.issue(memberDetails);
        testToken = BearerToken.substring(7);
    }

    @DisplayName("토큰 검증에 통과할 경우 어떠한 예외도 발생하지 않는다.")
    @Test
    void validate_token_success() {
        //when - then
        Assertions.assertThatCode(() -> jwtTokenManager.validateAccessToken(testToken))
                .doesNotThrowAnyException();
    }

    @DisplayName("유효 하지 않은 토큰은 다음과 같은 예외 메시지를 던진다.")
    @ParameterizedTest(name = "[{index}] 예외 메시지 : {1} | 토큰 값 : {0}")
    @CsvSource(value = {"N/A, 토큰이 없습니다.",
            "Bearer zs42w12893ujaksdnwqy8281.dasudlk21j31k.dasiduaoq1sdl, 유효한 토큰이 아닙니다."}
    , nullValues = "N/A")
    void validate_token_fail(String invalidToken, String message) {
        Assertions.assertThatThrownBy(() -> jwtTokenManager.validateAccessToken(invalidToken))
                .hasMessage(message);

    }

    /**
     * default 환경에서는 인터셉터를 통해 토큰의 유효함을 먼저 검증한다.
     * 해당 테스트에서는 토큰 매니저의 메서드가 잘 동작하는지 확인한다.
     */
    @DisplayName("토큰이 유효하다고 가정했을 때, 토큰 매니저를 통해 UserId, Email, Name 필드를 가져올 수 있다.")
    @Test
    void get_member_details() {
        //when
        MemberDetails memberDetails = jwtTokenManager.getMemberDetails(testToken);

        //then
        assertThat(memberDetails.getUserId()).isEqualTo(123l);
        assertThat(memberDetails.getEmail()).isEqualTo("leesin5498@naver.com");
        assertThat(memberDetails.getName()).isEqualTo("재헌");
    }
}