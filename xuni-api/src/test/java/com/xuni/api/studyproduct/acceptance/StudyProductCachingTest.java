package com.xuni.api.studyproduct.acceptance;

import com.xuni.api.studyproduct.infra.StudyProductRepository;
import com.xuni.common.domain.Category;
import com.xuni.api.studyproduct.application.StudyProductCreateService;
import com.xuni.api.studyproduct.application.StudyProductReadService;
import com.xuni.studyproduct.domain.StudyProduct;
import com.xuni.api.studyproduct.dto.request.StudyProductForm;
import com.xuni.api.studyproduct.dto.response.StudyProductContentReadResponse;
import com.xuni.api.studyproduct.dto.response.StudyProductReadResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StudyProductCachingTest {

    @Autowired
    StudyProductReadService studyProductReadService;
    @Autowired
    StudyProductRepository studyProductRepository;
    @Autowired
    StudyProductCreateService studyProductCreateService;
    @Autowired
    CacheManager cacheManager;

    String studyProductId = null;
    @BeforeEach
    void BeforeEach() {
        StudyProduct studyProduct1 = StudyProduct.builder()
                .name("JAVA의 정석")
                .creator("남궁성")
                .category(Category.JAVA)
                .thumbnail("image-url")
                .build();

        studyProduct1.putContent(1l, "객체");

        StudyProduct savedStudyProduct = studyProductRepository.save(studyProduct1);
        studyProductId = savedStudyProduct.getId();

        StudyProduct studyProduct2 = StudyProduct.builder()
                .name("모던 자바 인 액션")
                .creator("라울-게이브리얼 우르마")
                .category(Category.JAVA)
                .thumbnail("image-url")
                .build();

        studyProduct2.putContent(1l, "람다 표현식");

        StudyProduct studyProduct3 = StudyProduct.builder()
                .name("Real My SQL")
                .creator("박은영")
                .category(Category.MYSQL)
                .thumbnail("image-url")
                .build();

        studyProduct3.putContent(1l, "inno DB 엔진 스토리지");

        studyProductRepository.saveAll(List.of(studyProduct2, studyProduct3));
    }

    @AfterEach
    void afterEach() {
        studyProductRepository.deleteAll();
    }

    @DisplayName("식별자를 통해 스터디 상품을 조회할 경우 해당 식별자를 가진 스터디 상품은 캐시에 저장된다.")
    @Test
    void cache_hit_case_invoking_read_detail_by_id_method() {
        //when : readContent 호출 시 @Cacheable 로 인해 캐시 값 저장
        StudyProductContentReadResponse response = studyProductReadService.readDetailBy(studyProductId);
        //then
        StudyProductContentReadResponse cachedResponse = cacheManager.getCache("study-product")
                .get(studyProductId, StudyProductContentReadResponse.class);
        assertThat(response).isEqualTo(cachedResponse);
    }

    @DisplayName("카테고리를 통해 스터디 상품을 호출할 경우 해당 카테고리의 스터디 상품들은 캐시에 저장된다.")
    @Test
    void cache_hit_case_invoking_read_many_by_category_method() {
        //when
        List<StudyProductReadResponse> response = studyProductReadService.readManyBy(Category.JAVA);
        //then
        List<StudyProductReadResponse> cachedResponse = cacheManager.getCache("study-product-category")
                .get(Category.JAVA, List.class);

        assertThat(response).isEqualTo(cachedResponse);
    }

    @DisplayName("카테고리를 통해 스터디 상품을 호출할 경우 해당 카테고리의 스터디 상품들은 캐시에 저장된다.")
    @Test
    void cache_hit_case_invoking_read_many_method() {
        //when
        List<StudyProductReadResponse> response = studyProductReadService.readMany(PageRequest.of(0, 10));
        //then
        List<StudyProductReadResponse> cachedResponse = cacheManager.getCache("study-products")
                .get("0_10", List.class);

        assertThat(response).isEqualTo(cachedResponse);
    }

    @DisplayName("새 상품이 등록되면 해당 카테고리의 키 값을 가진 캐시 key 값이 삭제되어야 한다.")
    @Test
    void cache_evict_case_create_study_product() {
        //given - 조회할 시 캐시에 저장됨 위 테스트에서 검증했음
        studyProductReadService.readManyBy(Category.JAVA);
        studyProductReadService.readDetailBy(studyProductId);
        //when
        studyProductCreateService.create(new StudyProductForm("Effective Java" ,Category.JAVA, "조슈아 블랑"), null);
        //then
        List<StudyProductReadResponse> studyProductCategoryCache = cacheManager.getCache("study-product-category").get(Category.JAVA, List.class);
        assertThat(studyProductCategoryCache).isNull();

        List<StudyProductReadResponse> studyProductsCache = cacheManager.getCache("study-products").get("readMany", List.class);
        assertThat(studyProductsCache).isNull();
    }

    @DisplayName("다건 조회의 경우 캐시 key 값은 '{page}_{size}' 로 결정 된다. " +
            "따라서 page 혹은 size 가 다르다면 캐시 내용도 다르다.")
    @Test
    void cache_miss_diff_key() {
        //when : readContent 호출 시 @Cacheable 로 인해 캐시 값 저장
        List<StudyProductReadResponse> response1 = studyProductReadService.readMany(PageRequest.of(0, 1));
        List<StudyProductReadResponse> response2 = studyProductReadService.readMany(PageRequest.of(0, 2));
        //then
        List<StudyProductReadResponse> cachedResponse1 = cacheManager.getCache("study-products")
                .get("0_1", List.class);

        List<StudyProductReadResponse> cachedResponse2 = cacheManager.getCache("study-products")
                .get("0_2", List.class);

        assertThat(cachedResponse1).isEqualTo(response1);
        assertThat(cachedResponse2).isEqualTo(response2);
        assertThat(cachedResponse1).isNotEqualTo(cachedResponse2);
    }
}