package com.xuni.studyproduct.query;

import com.xuni.common.domain.Category;
import com.xuni.studyproduct.domain.StudyProduct;
import com.xuni.studyproduct.domain.StudyProductRepository;
import com.xuni.studyproduct.query.dynamic.StudyProductSearchCondition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyProductQueryImplTest {

    @Autowired
    StudyProductReadRepository studyProductReadRepository;
    @Autowired
    StudyProductRepository studyProductRepository;

    /** 총 30개의 데이터(객체) 생성
     * 객체는 아래와 같은 형식으로 필드가 생성됨
     * name = "name_{category}_{idx}"
     * creator = "creator_{category}_{idx}"
     * thumbnail = "thumbnail_{category}_{idx}"
     */

    @BeforeEach
    void beforeEach() {
        List<StudyProduct> studyProducts = new ArrayList<>();
        IntStream.range(0, 10).forEach(idx -> {
            studyProducts.addAll(List.of(
                    createStudyProduct(idx, Category.AWS),
                    createStudyProduct(idx, Category.JAVA),
                    createStudyProduct(idx, Category.NETWORK)));
                }
        );

        studyProductRepository.saveAll(studyProducts);
    }

    @AfterEach
    void afterEach() {
        studyProductRepository.deleteAll();
    }

    private static StudyProduct createStudyProduct(int idx, Category category) {
        return StudyProduct.builder()
                .name("name_" + category + "_" + idx)
                .creator("creator_" + category + "_" + idx)
                .thumbnail("thumbnail_" + category + "_" + idx)
                .category(category)
                .build();
    }

    @DisplayName("어떤 조건도 없을 때, 다시말해 StudyProductSearchCondition 객체의 인자가 모두 null일 때" +
            "size 만큼 반환한다.")
    @Test
    void search_study_product_basic() {
        StudyProductSearchCondition condition = new StudyProductSearchCondition(null, null, null);
        Page<StudyProductQueryResponse> pageResponse = studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(0, 20));

        List<StudyProductQueryResponse> content = pageResponse.getContent();
        assertThat(content.size()).isEqualTo(20);
    }

    @DisplayName("카테고리 조건에 맞는 데이터만 반환한다.")
    @ParameterizedTest
    @EnumSource(names = {"JAVA", "AWS", "NETWORK"})
    void search_study_product_category_cond(Category category) {
        StudyProductSearchCondition condition = new StudyProductSearchCondition(null, null, category);
        Page<StudyProductQueryResponse> pageResponse = studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(0, 20));

        List<StudyProductQueryResponse> content = pageResponse.getContent();
        Assertions.assertThat(content).extracting("category").containsOnly(category);
        assertThat(content.size()).isEqualTo(10);
    }

    @DisplayName("스터디 상품 이름 조건에 맞는 데이터만 반환한다." +
            "name 파라미터 값이 스터디 상품 name 필드 중 일부를 포함하고 있으면 조건을 충족한다." +
            "다르게 말하면 SQL 측면에서 LIKE = %name% 을 의미한다.")
    @ParameterizedTest
    @CsvSource(value = {"name_JAVA_0, 1", "name_NETWORK, 10"})
    void search_study_product_name_cond(String name, int size) {
        StudyProductSearchCondition condition = new StudyProductSearchCondition(name, null, null);
        Page<StudyProductQueryResponse> pageResponse = studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(0, 20));

        List<StudyProductQueryResponse> content = pageResponse.getContent();
        assertThat(content.size()).isEqualTo(size);
    }

    @DisplayName("스터디 상품 저자 조건에 맞는 데이터만 반환한다." +
            "저자 조건은 필드 명과 정확히 일치하는 경우에만 조건을 충족한다.")
    @Test
    void search_study_product_creator_cond() {
        String creatorCond = "creator_JAVA_0";
        StudyProductSearchCondition condition = new StudyProductSearchCondition(null, creatorCond, null);
        Page<StudyProductQueryResponse> pageResponse = studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(0, 20));

        List<StudyProductQueryResponse> content = pageResponse.getContent();
        assertThat(content.size()).isEqualTo(1);
        Assertions.assertThat(content).extracting("creator").containsExactly(creatorCond);
    }

    @DisplayName("스터디 상품 이름 조건을 만족하는 스터디 상품이 없을 경우 빈 리스트를 반환한다.")
    @Test
    void search_study_product_case_not_meet_name_cond() {
        String notMeetNameCond = "name_ETC_1000";
        StudyProductSearchCondition condition = new StudyProductSearchCondition(notMeetNameCond, null, null);
        Page<StudyProductQueryResponse> pageResponse = studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(0, 20));

        List<StudyProductQueryResponse> content = pageResponse.getContent();
        assertThat(content.size()).isEqualTo(0);
    }

    @DisplayName("스터디 상품 저자 조건을 만족하는 스터디 상품이 없을 경우 빈 리스트를 반환한다.")
    @Test
    void search_study_product_case_not_meet_creator_cond() {
        String notMeetCreatorCond = "creator_JAVA_1000";
        StudyProductSearchCondition condition = new StudyProductSearchCondition(null, notMeetCreatorCond, null);
        Page<StudyProductQueryResponse> pageResponse = studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(0, 20));

        List<StudyProductQueryResponse> content = pageResponse.getContent();
        assertThat(content.size()).isEqualTo(0);
    }

    @DisplayName("스터디 상품 카테고리 조건을 만족하는 스터디 상품이 없을 경우 빈 리스트를 반환한다.")
    @Test
    void search_study_product_case_not_meet_category_cond() {
        Category categoryCond = Category.ETC;
        StudyProductSearchCondition condition = new StudyProductSearchCondition(null, null, categoryCond);
        Page<StudyProductQueryResponse> pageResponse = studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(0, 20));

        List<StudyProductQueryResponse> content = pageResponse.getContent();
        assertThat(content.size()).isEqualTo(0);
    }

    @DisplayName("조건이 여러가지일 경우 모두 충족하는 스터디 상품을 반환한다.")
    @Test
    void search_study_product_complex_cond() {
        //given
        Category categoryCond = Category.JAVA;
        String nameCond = "name_JAVA";
        String creatorCond = "creator_JAVA_1";
        //when
        StudyProductSearchCondition condition = new StudyProductSearchCondition(nameCond, creatorCond, categoryCond);
        Page<StudyProductQueryResponse> pageResponse = studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(0, 20));

        List<StudyProductQueryResponse> content = pageResponse.getContent();
        assertThat(content.size()).isEqualTo(1);
    }
}