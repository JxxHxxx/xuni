package com.jxx.xuni.studyproduct.acceptance;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
import com.jxx.xuni.studyproduct.application.StudyProductReadService;
import com.jxx.xuni.studyproduct.domain.Content;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import com.jxx.xuni.studyproduct.dto.response.StudyProductContentReadResponse;
import com.jxx.xuni.studyproduct.dto.response.StudyProductReadResponse;
import com.jxx.xuni.support.ServiceCommon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.List;

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
    StudyProductCreateService studyProductCreateService;
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

    @DisplayName("호출한 적이 없는 스터디 상품은 캐시 저장소에 응답이 저장되어 있지 않다.")
    @Test
    void cache_miss() {
        //then
        StudyProductContentReadResponse cache = cacheManager.getCache("study-product")
                .get(studyProductId, StudyProductContentReadResponse.class);
        assertThat(cache).isNull();
    }

    @DisplayName("새 상품이 등록되면 해당 카테고리의 키 값을 가진 캐시 key 값이 삭제되어야 한다.")
    @Test
    void cache_evict() {
        //given - 조회할 시 캐시에 저장됨 위 테스트에서 검증했음
        List<StudyProductReadResponse> responses = studyProductReadService.readBy(Category.JAVA);
        //when
        studyProductCreateService.create(new StudyProductForm("Effective Java" ,Category.JAVA, "조슈아 블랑"), null);

        //then
        List<StudyProductReadResponse> evictedKeyOfCache = cacheManager.getCache("study-product-category").get(Category.JAVA, List.class);
        assertThat(evictedKeyOfCache).isNull();
    }
}