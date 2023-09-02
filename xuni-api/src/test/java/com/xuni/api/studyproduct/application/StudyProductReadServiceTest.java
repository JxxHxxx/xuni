package com.xuni.api.studyproduct.application;

import com.xuni.api.studyproduct.infra.StudyProductRepository;
import com.xuni.common.domain.Category;
import com.xuni.studyproduct.domain.Content;
import com.xuni.studyproduct.domain.StudyProduct;
import com.xuni.api.studyproduct.dto.response.StudyProductContentReadResponse;
import com.xuni.api.studyproduct.dto.response.StudyProductReadResponse;
import com.xuni.api.support.ServiceCommon;
import com.xuni.api.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ServiceTest
class StudyProductReadServiceTest extends ServiceCommon {

    @Autowired
    StudyProductReadService studyProductReadService;
    @Autowired
    StudyProductRepository studyProductRepository;

    @BeforeEach
    void beforeEach() {
        studyProductRepository.deleteAll();

        StudyProduct studyProduct1 = new StudyProduct("책1", "저자1", "썸네일1", Category.JAVA);
        StudyProduct studyProduct2 = new StudyProduct("책2", "저자2", "썸네일2", Category.JAVA);
        StudyProduct studyProduct3 = new StudyProduct("책3", "저자3", "썸네일3", Category.SPRING_FRAMEWORK);

        studyProductRepository.saveAll(List.of(studyProduct1, studyProduct2, studyProduct3));
    }

    @DisplayName("스터디 다건 조회 시, 설정한 size 갯수만큼 반환한다. 참고로 컨트롤러 단에서 size 의 갯수를 10개로 제한하고 있다.")
    @Test
    void read_many() {
        //given
        List<StudyProduct> studyProducts = new ArrayList<>();
        for (int i = 4; i < 14; i++) {
            StudyProduct studyProduct = new StudyProduct("서적" + i , "저자" + i, "썸네일" + i, Category.JAVA);
            studyProducts.add(studyProduct);
        }
        studyProductRepository.saveAll(studyProducts);
        //when
        PageRequest request = PageRequest.of(0, 10);
        List<StudyProductReadResponse> responses = studyProductReadService.readMany(request);
        //given
        assertThat(responses.size()).isEqualTo(10);
    }

    @DisplayName("특정 카테고리 검색 시 해당 카테고리의 스터디 상품만 조회 목록에 포함된다.")
    @Test
    void read_by_category() {
        //when
        List<StudyProductReadResponse> responses = studyProductReadService.readManyBy(Category.JAVA);
        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses).extracting("category").containsOnly(Category.JAVA);
        assertThat(responses).extracting("category").doesNotContain(Category.SPRING_FRAMEWORK);
    }

    @DisplayName("스터디 상품 식별자를 통해 컨텐트 검색 시 해당 상품의 컨텐트(목차, 제목)을 반환해야 한다.")
    @Test
    void readContent() {
        //given
        StudyProduct studyProduct = new StudyProduct("JAVA의 정석", "남궁성", "썸네일", Category.JAVA);
        studyProduct.getContents().add(new Content(1l, "객체 지향"));
        studyProduct.getContents().add(new Content(2l, "인터페이스"));
        studyProduct.getContents().add(new Content(3l, "변수 타입"));

        StudyProduct savedStudyProduct = studyProductRepository.save(studyProduct);
        String studyProductId = savedStudyProduct.getId();
        //when
        StudyProductContentReadResponse response = studyProductReadService.readDetailBy(studyProductId);
        //then
        assertThat(response.name()).isEqualTo("JAVA의 정석");
        assertThat(response.contents()).extracting("title").contains("객체 지향", "인터페이스","변수 타입");
    }
}