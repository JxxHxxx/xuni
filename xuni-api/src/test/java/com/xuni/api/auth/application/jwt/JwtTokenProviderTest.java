package com.xuni.api.auth.application.jwt;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.application.SimpleMemberDetails;
import com.xuni.api.auth.application.jwt.JwtTokenProvider;
import com.xuni.api.support.ServiceOnlyTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceOnlyTest
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @DisplayName("토큰 생성 검증")
    @Test
    void issueToken() {
        MemberDetails memberDetails = new SimpleMemberDetails(123l, "leesin5498@naver.com", "재헌");
        String token = jwtTokenProvider.issue(memberDetails);

        Assertions.assertThat(token).isNotNull();
    }
}