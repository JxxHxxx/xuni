package com.jxx.xuni.auth.support;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;

@Service
public class JwtTokenManager {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final String CLAIMS_MEMBER_DETAILS_KEY = "memberDetails";

    public void validateAccessToken(String token) {
        checkNull(token);
        checkTokenValidation(token);
    }

    public MemberDetails getMemberDetails(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        Object memberClaims = claims.get(CLAIMS_MEMBER_DETAILS_KEY);
        return claimsToMemberDetails(memberClaims);
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
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return isExpired(claims);
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
        String name = (String)claimsElements.get("name");

        return new SimpleMemberDetails(userId, email, name);
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
