package com.jxx.xuni.statistics.presentation;


import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.statistics.application.MemberStatisticsService;
import com.jxx.xuni.statistics.domain.MemberStatisticsRepository;
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
