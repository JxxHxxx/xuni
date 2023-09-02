package com.xuni.api.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuni.api.auth.application.AuthMailService;
import com.xuni.api.auth.application.AuthService;
import com.xuni.api.auth.application.GoogleClient;
import com.xuni.api.auth.application.PasswordEncoder;
import com.xuni.api.auth.infra.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @MockBean
    GoogleClient googleClient;
    
}
