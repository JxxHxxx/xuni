package com.jxx.xuni.auth.support;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final String CLAIMS_MEMBER_DETAILS_KEY = "memberDetails";
    private static final String BEARER_PREFIX = "Bearer ";

    static final long VALID_TIME = 24 * 60 * 60 * 1000L;

    public String issue(MemberDetails memberDetails) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALID_TIME);

        return BEARER_PREFIX + Jwts.builder()
                .setClaims(makeClaims(memberDetails))
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Claims makeClaims(MemberDetails memberDetails) {
        Map<String, Object> claimsMap = new HashMap<>();
        Object objectMemberDetails = memberDetails;
        claimsMap.put(CLAIMS_MEMBER_DETAILS_KEY, objectMemberDetails);

        return Jwts.claims(claimsMap);
    }
}
