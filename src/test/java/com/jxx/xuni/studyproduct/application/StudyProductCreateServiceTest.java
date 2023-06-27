package com.jxx.xuni.studyproduct.application;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.domain.*;
import com.jxx.xuni.studyproduct.dto.request.StudyProductContentForm;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.NOT_EXIST_STUDY_PRODUCT;

@ServiceTest
class StudyProductCreateServiceTest extends ServiceCommon {

    @Autowired
    StudyProductRepository studyProductRepository;
    @Autowired
    StudyProductCreateService studyProductCreateService;

    @DisplayName("스터디 상품 저장")
    @Test
    void create_role() {
        //given
        StudyProduct studyProduct1 = new StudyProduct("JAVA의 정석", "남궁성", "tbn", Category.JAVA);

        //when
        String studyProductId = studyProductRepository.save(studyProduct1).getId();

        //then
        Assertions.assertThat(studyProductRepository.findById(studyProductId)).isPresent();
    }

    @DisplayName("스터디 상품 상세 생성 규칙 " +
            "1. createDetail()는 StudyProductDetail 를 생성한다. 데이터 베이스를 조회했을 시 StudyProductDetail가 저장되어야 한다." +
            "2. createDetail()는 호출 시 마다 StudyProductDetail chapterId를 1부터 매핑한다. 즉 chapterId는 고유하지 않다.")
    @Test
    void create_detail_success_and_role() {
        //given - 2번 검증을 위해 study-product 2개 생성
        StudyProduct studyProduct1 = new StudyProduct("JAVA의 정석", "남궁성", "tbn", Category.JAVA);
        String studyProductId1 = studyProductRepository.save(studyProduct1).getId();

        List<StudyProductContentForm> form1 = List.of(
                new StudyProductContentForm("변수"),
                new StudyProductContentForm("클래스"),
                new StudyProductContentForm("인터페이스"));

        StudyProduct studyProduct2 = new StudyProduct("초보 웹 개발자를 위한 스프링5 프로그래밍 입문", "최범균", "tbn", Category.JAVA);
        String studyProductId2 = studyProductRepository.save(studyProduct2).getId();

        List<StudyProductContentForm> form2 = List.of(
                new StudyProductContentForm("IOC"),
                new StudyProductContentForm("AOP"),
                new StudyProductContentForm("MVC"));

        //when
        studyProductCreateService.putContent(studyProductId1, form1);
        studyProductCreateService.putContent(studyProductId2, form2);

        //then
        StudyProduct findStudyProduct1 = studyProductRepository.findById(studyProductId1).get();
        StudyProduct findStudyProduct2 = studyProductRepository.findById(studyProductId2).get();

        List<Long> studyProduct1ChapterId = findStudyProduct1.getContents()
                .stream().map(d -> d.getChapterNo()).toList();
        List<Long> studyProduct2ChapterId = findStudyProduct2.getContents()
                .stream().map(d -> d.getChapterNo()).toList();

        Assertions.assertThat(studyProduct1ChapterId).containsAnyElementsOf(studyProduct2ChapterId);
    }

    @DisplayName("스터디 상품 상세 생성 시, 스터디 상품 식별자가 존재하지 않으면 " +
            "IllegalArgumentException 예외가 발생한다.")
    @Test
    void create_detail_fila_cause_not_exist_study_product_id() {
        //given
        String notExistStudyProductId = "not-Exist-Study-Product-Id";
        List<StudyProductContentForm> form1 = List.of(
                new StudyProductContentForm("변수"),
                new StudyProductContentForm("클래스"),
                new StudyProductContentForm("인터페이스"));

        //when - then
        Assertions.assertThatThrownBy(() -> studyProductCreateService.putContent(notExistStudyProductId, form1))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage(NOT_EXIST_STUDY_PRODUCT);
    }
}