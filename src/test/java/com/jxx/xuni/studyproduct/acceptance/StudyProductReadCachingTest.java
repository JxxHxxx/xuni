package com.jxx.xuni.studyproduct.acceptance;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.application.StudyProductReadService;
import com.jxx.xuni.studyproduct.domain.Content;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import com.jxx.xuni.studyproduct.dto.response.StudyProductContentReadResponse;
import com.jxx.xuni.support.ServiceCommon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.*;

/**
 * 캐시 적용 여부를 검증합니다.
 */

@SpringBootTest
class StudyProductReadCachingTest extends ServiceCommon {

    @Autowired
    StudyProductReadService studyProductReadService;
    @Autowired
    StudyProductRepository studyProductRepository;
    @Autowired
    CacheManager cacheManager;

    String studyProductId = null;
    @BeforeEach
    void BeforeEach() {
        StudyProduct studyProduct = StudyProduct.builder()
                .name("JAVA의 정석")
                .creator("남궁성")
                .category(Category.JAVA)
                .thumbnail("image-url")
                .build();

        studyProduct.getContents().add(new Content(1l, "객체"));
        StudyProduct savedStudyProduct = studyProductRepository.save(studyProduct);
        studyProductId = savedStudyProduct.getId();
    }

    @DisplayName("이미 호출한 적이 있는 스터디 상품은 캐시 저장소에 응답이 저장된다.")
    @Test
    void cache_hit() {
        //when
        StudyProductContentReadResponse response = studyProductReadService.readContent(studyProductId);
        //then
        StudyProductContentReadResponse cacheResponse = cacheManager.getCache("study-product")
                .get(studyProductId, StudyProductContentReadResponse.class);

        assertThat(response).isEqualTo(cacheResponse);
    }

    @DisplayName("이미 호출한 적이 없는 스터디 상품은 캐시 저장소에 응답이 저장되어 있지 않다.")
    @Test
    void cache_miss() {
        //then
        StudyProductContentReadResponse cache = cacheManager.getCache("study-product")
                .get(studyProductId, StudyProductContentReadResponse.class);

        assertThat(cache).isNull();
    }
}