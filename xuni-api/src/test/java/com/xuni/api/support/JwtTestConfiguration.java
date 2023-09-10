package com.xuni.api.support;

import com.xuni.api.auth.application.jwt.JwtTokenManager;
import com.xuni.api.auth.application.jwt.JwtTokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@TestConfiguration
public class JwtTestConfiguration {

    @Bean(name = "testJwtTokenProvider")
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean(name = "testJwtTokenManager")
    JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager();
    }

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
