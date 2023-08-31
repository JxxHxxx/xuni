package com.xuni.api.auth.support;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.application.SimpleMemberDetails;
import com.xuni.auth.domain.exception.ExpiredTokenException;
import com.xuni.common.support.ServiceOnly;
import com.xuni.auth.domain.Authority;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.LinkedHashMap;

import static com.xuni.auth.domain.Authority.*;

@Slf4j
@ServiceOnly
public class JwtTokenManager {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final String CLAIMS_MEMBER_DETAILS_KEY = "memberDetails";
    private static final String TOKEN_PREFIX = "Bearer ";

    public MemberDetails getMemberDetails(String token) {
        String pureToken = removeBearerFrom(token);
        validate(pureToken);

        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(pureToken).getBody();
        Object memberClaims = claims.get(CLAIMS_MEMBER_DETAILS_KEY);
        return claimsToMemberDetails(memberClaims);
    }

    public void validate(String token) {
        checkNull(token);
        checkTokenValidation(token);
    }

    /**
     * jwt token 형태는 Bearer_xxx.xxx.xxx 입니다.
     * prefix인 Bearer_ 를 떼어내 xxx.xxx.xxx 를 반환하는 작업
     *
     * 참고 : Bearer 뒤에 붙은 _ 는 공백을 의미한다.
     * 작성일시 : 2023-07-07
     * 작성자 : jxxHxxx
     */
    public String removeBearerFrom(String token) {
        checkPrefixOf(token);
        return token.substring(TOKEN_PREFIX.length());
    }

    private void checkPrefixOf(String token) {
        if(!token.startsWith(TOKEN_PREFIX) || token.isBlank()) {
            throw new IllegalArgumentException("헤더 값의 형식이 올바르지 못합니다.");
        }
    }

    private void checkNull(String token) {
        if (token == null) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
    }

    private void checkTokenValidation(String token) {
        if (isInvalidToken(token)) {
            throw new IllegalArgumentException("유효한 토큰이 아닙니다.");
        }
    }

    private boolean isInvalidToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

            return false;
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            throw new ExpiredTokenException("만료된 토큰입니다.");
        } catch (JwtException e) {
            return true;
        }
    }

    private boolean isExpired(Jws<Claims> claims) {
        return claims.getBody().getExpiration().before(new Date());
    }

    private MemberDetails claimsToMemberDetails(Object claims) {
        LinkedHashMap<String, Object> claimsElements = (LinkedHashMap) claims;
        Long userId = castToLongId(claimsElements);
        String email = (String) claimsElements.get("email");
        String name = (String) claimsElements.get("name");
        Authority authority = castToStringAuthority(claimsElements);

        return new SimpleMemberDetails(userId, email, name, authority);
    }

    private Authority castToStringAuthority(LinkedHashMap<String, Object> claimsElements) {
        String stringAuthority = (String) claimsElements.get("authority");
        if(ADMIN.toString().equals(stringAuthority)) {
            return ADMIN;
        }
        return USER;
    }


    //TODO : 원인 분석 필요
    // 인증 객체 유저의 Long Type userId 값이 int Typedml 최대값(2,147,483,647)미만이면
    // 자동으로 int로 변환되어 가져오게 된다.
    // 이로 인해 classCastException 막기 위해 아래 로직이 필요하다.

    private Long castToLongId(LinkedHashMap<String, Object> claimsElements) {
        Long longValue;
        try {
            Integer userId = (Integer) claimsElements.get("userId");
            longValue = Long.valueOf(userId.longValue());
        } catch (ClassCastException e) {
            longValue = (Long) claimsElements.get("userId");
        }
        return longValue;
    }
}
