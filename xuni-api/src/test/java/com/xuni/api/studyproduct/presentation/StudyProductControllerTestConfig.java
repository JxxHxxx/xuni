package com.xuni.api.studyproduct.presentation;

import com.xuni.auth.support.JwtTokenManager;
import com.xuni.auth.support.JwtTokenProvider;
import com.xuni.api.common.service.AmazonS3Handler;
import com.xuni.common.query.PageConverter;
import com.xuni.api.studyproduct.application.StudyProductCreateService;
import com.xuni.api.studyproduct.application.StudyProductReadService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@TestConfiguration
public class StudyProductControllerTestConfig {
    @MockBean
    StudyProductCreateService studyProductCreateService;
    @MockBean
    StudyProductReadService studyProductReadService;
    @MockBean
    PageConverter pageConverter;
    @MockBean
    AmazonS3Handler amazonS3Handler;
    @Bean
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
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
