package com.jxx.xuni.auth.support;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.domain.exception.ExpiredTokenException;
import com.jxx.xuni.support.ServiceOnlyTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@ServiceOnlyTest
class JwtTokenManagerTest {

    @Autowired
    JwtTokenManager jwtTokenManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    String token;
    MemberDetails memberDetails;
    @Value("${jwt.secret.key}")
    private String secretKey;

    @BeforeEach
        // class given
    void beforeEach() {
        memberDetails = new SimpleMemberDetails(123l, "leesin5498@naver.com", "재헌");
        token = jwtTokenProvider.issue(memberDetails);

    }

    @DisplayName("토큰 검증에 통과할 경우 어떠한 예외도 발생하지 않는다.")
    @Test
    void validate_token_success() {
        String pureToken = jwtTokenManager.removeBearerFrom(token);
        //when - then
        assertThatCode(() -> jwtTokenManager.validate(pureToken))
                .doesNotThrowAnyException();
    }

    @DisplayName("유효 하지 않은 토큰은 다음과 같은 예외 메시지를 던진다.")
    @ParameterizedTest(name = "[{index}] 예외 메시지 : {1} | 토큰 값 : {0}")
    @CsvSource(value = {"N/A, 토큰이 없습니다.",
            "zs42w12893ujaksdnwqy8281.dasudlk21j31k.dasiduaoq1sdl, 유효한 토큰이 아닙니다."}
            , nullValues = "N/A")
    void validate_token_fail(String invalidToken, String message) {
        assertThatThrownBy(() -> jwtTokenManager.validate(invalidToken))
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
        MemberDetails memberDetails = jwtTokenManager.getMemberDetails(token);

        //then
        assertThat(memberDetails.getUserId()).isEqualTo(123l);
        assertThat(memberDetails.getEmail()).isEqualTo("leesin5498@naver.com");
        assertThat(memberDetails.getName()).isEqualTo("재헌");
    }

    @DisplayName("토큰은 Bearer_xxx.xxx.xxx 형태이다. 메서드 실행 시 Bearer_이 떼어진 xxx.xxx.xxx 형태를 반환한다. " +
            "_ 은 공백을 의미한다.")
    @Test
    void extract_token_from_bearer() {
        String tokenPrefix = "Bearer ";
        //given
        MemberDetails memberDetails = new SimpleMemberDetails(11l, "leesin5498@xuni.com", "xuni");
        String bearerToken = jwtTokenProvider.issue(memberDetails);
        assertThat(bearerToken).startsWith(tokenPrefix);
        //when
        String extractedToken = jwtTokenManager.removeBearerFrom(bearerToken);
        //then
        assertThat(extractedToken).doesNotContain(tokenPrefix);
    }

    @DisplayName("토큰의 접두사가 Bearer_이 아닐 경우 " +
            "IllegalArgumentException 예외 " +
            "헤더 값의 형식이 올바르지 못합니다. 예외 메시지를 반환한다.")
    @Test
    void extract_token_from_bearer_fail_cause_prefix_is_not_Bearer_() {
        //given
        String isNotBearerToken = "doesNotBearer_xxx.xxx.xxx";
        //when - then
        assertThatThrownBy(() -> jwtTokenManager.removeBearerFrom(isNotBearerToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("헤더 값의 형식이 올바르지 못합니다.");
    }

    @DisplayName("만료된 토큰을 들고 검증을 수행할 경우" +
            "예외 ExpiredTokenException 발생," +
            "메시지 만료된 토큰입니다.")
    @Test
    void validate_access_token_fail_cuz_expired_token() throws InterruptedException {
        Claims claims = jwtTokenProvider.makeClaims(memberDetails);
        Date now = new Date(); // exiredTime

        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        Thread.sleep(1l);

        //when - then
        assertThatThrownBy(() -> jwtTokenManager.validate(token))
                .isInstanceOf(ExpiredTokenException.class)
                .hasMessage("만료된 토큰입니다.");
    }
}