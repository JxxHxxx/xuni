package com.jxx.xuni.statistics.application;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.statistics.domain.StudyProductStatisticsRepository;
import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import com.jxx.xuni.studyproduct.dto.response.StudyProductCreateResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudyProductNotifierTest {
    @Autowired
    StudyProductCreateService studyProductCreateService;
    @Autowired
    StudyProductRepository studyProductRepository;
    @Autowired
    StudyProductStatisticsRepository studyProductStatisticsRepository;

    @DisplayName("스터디 상품 생성 이벤트 처리 " +
            "스터디 상품이 정상적으로 생성될 때 - 스터디 상품 통계 엔티티가 생성된다.")
    @Test
    void study_product_create_event() {
        //when
        StudyProductCreateResponse response = studyProductCreateService.create(
                new StudyProductForm("남궁성", Category.JAVA, "남궁성"), "img-url");

        String studyProductId = response.studyProductId();
        //then
        Assertions.assertThat(studyProductStatisticsRepository.findById(studyProductId)).isPresent();
    }
}