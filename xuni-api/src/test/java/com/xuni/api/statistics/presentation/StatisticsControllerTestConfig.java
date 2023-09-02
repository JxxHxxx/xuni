package com.xuni.api.statistics.presentation;


import com.xuni.api.statistics.application.MemberStatisticsService;
import com.xuni.api.statistics.application.StudyProductStatisticsService;
import com.xuni.api.statistics.infra.MemberStatisticsRepository;
import com.xuni.api.statistics.infra.StudyProductStatisticsRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;


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

}
