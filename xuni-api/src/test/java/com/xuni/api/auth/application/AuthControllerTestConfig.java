package com.xuni.api.auth.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuni.auth.domain.MemberRepository;
import com.xuni.api.auth.support.JwtTokenManager;
import com.xuni.api.auth.support.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@TestConfiguration
public class AuthControllerTestConfig {
    @MockBean
    AuthService authService;
    @MockBean
    AuthMailService authMailService;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    PasswordEncoder passwordEncoder;
    @Autowired
    ObjectMapper objectMapper;

    @Bean
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager();
    }

    @Bean
    GoogleClient googleClient() {
        return new GoogleClient(objectMapper, memberRepository,passwordEncoder);
    }

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
