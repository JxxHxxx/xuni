package com.xuni.api.studyproduct.application;

import com.xuni.common.domain.Category;
import com.xuni.common.query.PagingModifier;
import com.xuni.common.query.ModifiedPagingForm;
import com.xuni.studyproduct.domain.Content;
import com.xuni.studyproduct.domain.StudyProduct;
import com.xuni.api.studyproduct.dto.response.StudyProductContentReadResponse;
import com.xuni.api.studyproduct.dto.response.StudyProductReadResponse;
import com.xuni.studyproduct.query.StudyProductQueryResponse;
import com.xuni.studyproduct.query.StudyProductReadRepository;
import com.xuni.studyproduct.query.dynamic.StudyProductSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class StudyProductReadService {

    private final StudyProductReadRepository studyProductReadRepository;
    private final PagingModifier pagingModifier;

    //TODO : 캐시 Key 설계 잘 못 했음
    @Cacheable(cacheNames = "study-products", key = "#pageable.pageNumber + '_' + #pageable.pageSize", cacheManager = "localCacheManager")
    public List<StudyProductReadResponse> readMany(Pageable pageable) {
        Page<StudyProduct> studyProducts = studyProductReadRepository.readBy(pageable);

        return studyProducts.stream().map(studyProduct -> new StudyProductReadResponse(
                studyProduct.getId(),
                studyProduct.getName(),
                studyProduct.getCategory(),
                studyProduct.getCreator(),
                studyProduct.getThumbnail())).toList();
    }

    @Cacheable(cacheNames = "study-product-category", key = "#category", cacheManager = "localCacheManager")
    public List<StudyProductReadResponse> readManyBy(Category category) {
        List<StudyProduct> studyProducts = studyProductReadRepository.findStudyProductByCategory(category);

        return studyProducts.stream().map(studyProduct -> new StudyProductReadResponse(
                studyProduct.getId(),
                studyProduct.getName(),
                studyProduct.getCategory(),
                studyProduct.getCreator(),
                studyProduct.getThumbnail())).toList();
    }

    // TODO : 상품 목차가 존재하지 않을 때 해당 예외를 던지는게 올바른지 판단해야 함
    @Cacheable(cacheNames = "study-product", key="#studyProductId", cacheManager = "localCacheManager")
    public StudyProductContentReadResponse readDetailBy(String studyProductId) {
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

    public Page<StudyProductQueryResponse> searchStudyProduct(StudyProductSearchCondition condition, int page, int size) {
        ModifiedPagingForm form = pagingModifier.modify(page, size);
        return studyProductReadRepository.searchStudyProduct(condition, PageRequest.of(form.page(), form.size()));
    }
}