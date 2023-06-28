package com.jxx.xuni.statistics.application;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.common.event.trigger.statistics.ReviewCreatedEvent;
import com.jxx.xuni.common.event.trigger.statistics.ReviewDeletedEvent;
import com.jxx.xuni.common.event.trigger.statistics.ReviewUpdatedEvent;
import com.jxx.xuni.statistics.domain.StudyProductStatistics;
import com.jxx.xuni.statistics.domain.StudyProductStatisticsRepository;
import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import com.jxx.xuni.studyproduct.dto.response.StudyProductCreateResponse;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class StudyProductNotifierTest extends ServiceCommon {
    @Autowired
    StudyProductCreateService studyProductCreateService;
    @Autowired
    StudyProductRepository studyProductRepository;
    @Autowired
    StudyProductStatisticsRepository studyProductStatisticsRepository;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    String studyProductId = null;
    @BeforeEach
    void beforeEach() {
        StudyProduct studyProduct = StudyProduct.builder()
                .name("JAVA의 정석")
                .creator("남궁성")
                .category(Category.JAVA)
                .thumbnail("img-url")
                .build();
        StudyProduct savedStudyProduct = studyProductRepository.save(studyProduct);
        studyProductId = savedStudyProduct.getId();
    }

    @DisplayName("스터디 상품 생성 이벤트 처리 " +
            "스터디 상품이 정상적으로 생성될 때 - 스터디 상품 통계 엔티티가 생성된다.")
    @Test
    void study_product_create_event() {
        //when
        StudyProductCreateResponse response = studyProductCreateService.create(
                new StudyProductForm("남궁성", Category.JAVA, "남궁성"), "img-url");
        TestTransaction.flagForCommit();
        TestTransaction.end();
        //then
        assertThat(studyProductStatisticsRepository.findById(response.studyProductId())).isPresent();
    }

    @DisplayName("리뷰 생성 이벤트 처리 " +
            "리뷰가 생성될 경우 - 스터디 상품 통계 엔티티에 해당 내용이 반영된다." +
            "1. ReviewCnt += 1" +
            "2. ReviewSum + Rating")
    @Test
    void review_created_event() {
        //given
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id(studyProductId)
                .reviewCnt(0)
                .ratingSum(0)
                .build();

        studyProductStatisticsRepository.save(statistics);
        //when
        ReviewCreatedEvent event = new ReviewCreatedEvent(studyProductId, 3);
        eventPublisher.publishEvent(event);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        StudyProductStatistics updateStatistics = studyProductStatisticsRepository.findById(studyProductId).get();
        //then
        assertThat(updateStatistics.getReviewCnt()).isEqualTo(1);
        assertThat(updateStatistics.getRatingSum()).isEqualTo(3);
    }

    @DisplayName("리뷰 수정 이벤트 처리 " +
            "리뷰가 수정될 경우 - 스터디 상품 통계 엔티티에 해당 내용이 반영된다." +
            "1. ReviewCnt 변화 없음" +
            "2. ReviewSum += 수정된 평점 - 이전 평점 ")
    @Test
    void review_updated_event() {
        //given
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id(studyProductId)
                .reviewCnt(1)
                .ratingSum(3)
                .build();

        studyProductStatisticsRepository.save(statistics);
        //when
        ReviewUpdatedEvent event = new ReviewUpdatedEvent(studyProductId, 3, 5);
        eventPublisher.publishEvent(event);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        //then
        StudyProductStatistics updateStatistics = studyProductStatisticsRepository.findById(studyProductId).get();
        //then
        assertThat(updateStatistics.getReviewCnt()).isEqualTo(1);
        assertThat(updateStatistics.getRatingSum()).isEqualTo(5);
    }

    @DisplayName("리뷰 삭제 이벤트 처리 " +
            "리뷰가 삭제될 경우 - 스터디 상품 통계 엔티티에 해당 내용이 반영된다." +
            "1. ReviewCnt -= 1" +
            "2. ReviewSum -= 평점")
    @Test
    void review_deleted_event() {
        //given
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id(studyProductId)
                .reviewCnt(1)
                .ratingSum(3)
                .build();

        studyProductStatisticsRepository.save(statistics);
        //when
        ReviewDeletedEvent event = new ReviewDeletedEvent(studyProductId, 3);
        eventPublisher.publishEvent(event);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        //then
        StudyProductStatistics updateStatistics = studyProductStatisticsRepository.findById(studyProductId).get();
        //then
        assertThat(updateStatistics.getReviewCnt()).isEqualTo(0);
        assertThat(updateStatistics.getRatingSum()).isEqualTo(0);
    }
}