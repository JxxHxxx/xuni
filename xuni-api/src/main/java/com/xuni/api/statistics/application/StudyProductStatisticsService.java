package com.xuni.api.statistics.application;

import com.xuni.api.statistics.dto.response.StudyProductStatisticsReadResponse;
import com.xuni.statistics.domain.StudyProductStatistics;
import com.xuni.api.statistics.infra.StudyProductStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class StudyProductStatisticsService {

    private static final Integer RATING_SUM_INIT = 0;
    private static final Integer REVIEW_CNT_INIT = 0;

    private final StudyProductStatisticsRepository studyProductStatisticsRepository;

    public void create(String studyProductId) {
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id(studyProductId)
                .ratingSum(RATING_SUM_INIT)
                .reviewCnt(REVIEW_CNT_INIT).build();

        studyProductStatisticsRepository.save(statistics);
    }

    public List<StudyProductStatisticsReadResponse> readBy(Pageable pageable) {
        Page<StudyProductStatistics> statistics = studyProductStatisticsRepository.readBy(pageable);

        return statistics.stream().map(st -> new StudyProductStatisticsReadResponse(
                st.getId(),
                st.getRatingSum(),
                st.getReviewCnt()
        )).toList();
    }

    public StudyProductStatisticsReadResponse readOne(String studyProductId) {
        StudyProductStatistics statistics = studyProductStatisticsRepository.findById(studyProductId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));

        return new StudyProductStatisticsReadResponse(
                statistics.getId(),
                statistics.getRatingSum(),
                statistics.getReviewCnt());
    }

    @Transactional
    public void reflectReviewCreate(Integer rating, String studyProductId) {
        StudyProductStatistics statistics = studyProductStatisticsRepository.findById(studyProductId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));

        statistics.add(rating);
    }

    @Transactional
    public void reflectReviewUpdate(String studyProductId, Integer ratingBeforeUpdate, Integer updatedRating) {
        StudyProductStatistics statistics = studyProductStatisticsRepository.findById(studyProductId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));

        statistics.update(ratingBeforeUpdate, updatedRating);
    }

    @Transactional
    public void reflectReviewDelete(String studyProductId, Integer rating) {
        StudyProductStatistics statistics = studyProductStatisticsRepository.findById(studyProductId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));

        statistics.delete(rating);
    }
}
