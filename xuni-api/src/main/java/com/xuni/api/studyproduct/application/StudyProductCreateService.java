package com.xuni.api.studyproduct.application;

import com.xuni.common.event.trigger.StudyProductCreatedEvent;
import com.xuni.studyproduct.domain.StudyProduct;
import com.xuni.api.studyproduct.infra.StudyProductRepository;
import com.xuni.api.studyproduct.dto.request.StudyProductContentForm;
import com.xuni.api.studyproduct.dto.request.StudyProductForm;
import com.xuni.api.studyproduct.dto.response.StudyProductCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.xuni.api.studyproduct.dto.response.StudyProductApiMessage.NOT_EXIST_STUDY_PRODUCT;

@Service
@RequiredArgsConstructor
public class StudyProductCreateService {

    private final StudyProductRepository studyProductRepository;
    private final CacheManager cacheManager;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Caching(evict = {@CacheEvict(cacheNames = "study-products", allEntries = true, cacheManager = "localCacheManager"),
                      @CacheEvict(cacheNames="study-product-category", key="#form.category()", cacheManager = "localCacheManager")})
    public StudyProductCreateResponse create(StudyProductForm form, String imageURL) {
        StudyProduct studyProduct = StudyProduct.builder()
                .name(form.name())
                .creator(form.creator())
                .thumbnail(imageURL)
                .category(form.category())
                .build();

        StudyProduct savedProduct = studyProductRepository.save(studyProduct);

        StudyProductCreatedEvent event = new StudyProductCreatedEvent(savedProduct.getId());
        eventPublisher.publishEvent(event);

        return new StudyProductCreateResponse(savedProduct.getId());
    }

    // TODO : 일반적인 흐름 상 create -> putContent 는 필연적임, 그런데 현재 구조 상 캐시가 2번 초기화됨 의미없는 초기화가 발생 해결채 필요.
    @Transactional
    @Caching(evict = {@CacheEvict(cacheNames = "study-products", allEntries = true, cacheManager = "localCacheManager"),
                      @CacheEvict(cacheNames="study-product", key="#studyProductId", cacheManager = "localCacheManager")})
    public void putContent(String studyProductId, List<StudyProductContentForm> contentForms) {
        StudyProduct studyProduct = studyProductRepository.findById(studyProductId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_STUDY_PRODUCT));

        Long chapterNoSequence = 1l;
        for (StudyProductContentForm form : contentForms) {
            studyProduct.putContent(chapterNoSequence++, form.title());
        }

        Cache cache = cacheManager.getCache("study-product-category");
        cache.evict(studyProduct.getCategory());
    }
}