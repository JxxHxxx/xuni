package com.jxx.xuni.auth.support;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.support.ServiceOnlyTest;
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