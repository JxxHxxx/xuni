package com.jxx.xuni.studyproduct;

import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class StudyProductControllerTestConfig implements WebMvcConfigurer {
    @MockBean
    StudyProductCreateService studyProductCreateService;

    @Bean
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }
    @Bean
    JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager();
    }
}
