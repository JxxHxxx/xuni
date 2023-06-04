package com.jxx.xuni.statistics.application;

import com.jxx.xuni.statistics.domain.StudyProductStatistics;
import com.jxx.xuni.statistics.domain.StudyProductStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyProductStatisticsService {

    private static final Integer RATING_AVG_INIT = 0;
    private static final Integer REVIEW_CNT_INIT = 0;

    private final StudyProductStatisticsRepository studyProductStatisticsRepository;

    public void create(String studyProductId) {
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id(studyProductId)
                .ratingAvg(RATING_AVG_INIT)
                .reviewCnt(REVIEW_CNT_INIT).build();

        studyProductStatisticsRepository.save(statistics);
    }

}
