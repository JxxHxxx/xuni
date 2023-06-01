package com.jxx.xuni.studyproduct.application;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.domain.Content;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.dto.response.StudyProductContentReadResponse;
import com.jxx.xuni.studyproduct.dto.response.StudyProductReadResponse;
import com.jxx.xuni.studyproduct.query.StudyProductReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class StudyProductReadService {

    private final StudyProductReadRepository studyProductReadRepository;

    @Cacheable(cacheNames = "study-products", cacheManager = "localCacheManager")
    public List<StudyProductReadResponse> readBy(Pageable pageable) {
        Page<StudyProduct> studyProducts = studyProductReadRepository.readBy(pageable);

        return studyProducts.stream().map(sp -> new StudyProductReadResponse(
                sp.getId(),
                sp.getName(),
                sp.getCategory(),
                sp.getCreator(),
                sp.getThumbnail())).toList();
    }

    @Cacheable(cacheNames = "study-product-category", key = "#category", cacheManager = "localCacheManager")
    public List<StudyProductReadResponse> readBy(Category category) {
        List<StudyProduct> studyProducts = studyProductReadRepository.findStudyProductByCategory(category);

        return studyProducts.stream().map(sp -> new StudyProductReadResponse(
                sp.getId(),
                sp.getName(),
                sp.getCategory(),
                sp.getCreator(),
                sp.getThumbnail())).toList();
    }

    // TODO : 상품 목차가 존재하지 않을 때 해당 예외를 던지는게 올바른지 판단해야 함
    @Cacheable(cacheNames = "study-product", key="#studyProductId", cacheManager = "localCacheManager")
    public StudyProductContentReadResponse readContent(String studyProductId) {
        StudyProduct studyProduct = studyProductReadRepository.readWithContentFetch(studyProductId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));

        List<Content> contents = studyProduct.getContents();

        return new StudyProductContentReadResponse(
                studyProduct.getName(),
                studyProduct.getCategory(),
                studyProduct.getCreator(),
                studyProduct.getThumbnail(),
                contents);
    }
}