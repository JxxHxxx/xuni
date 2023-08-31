package com.xuni.api.statistics.presentation;


import com.xuni.auth.support.JwtTokenManager;
import com.xuni.auth.support.JwtTokenProvider;
import com.xuni.api.statistics.application.MemberStatisticsService;
import com.xuni.api.statistics.application.StudyProductStatisticsService;
import com.xuni.statistics.domain.MemberStatisticsRepository;
import com.xuni.statistics.domain.StudyProductStatisticsRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@TestConfiguration
public class StatisticsControllerTestConfig {
    @MockBean
    MemberStatisticsRepository memberStatisticsRepository;
    @MockBean
    MemberStatisticsService memberStatisticsService;
    @MockBean
    StudyProductStatisticsRepository studyProductStatisticsRepository;
    @MockBean
    StudyProductStatisticsService studyProductStatisticsService;
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
