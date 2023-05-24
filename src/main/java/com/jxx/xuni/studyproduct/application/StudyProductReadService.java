package com.jxx.xuni.studyproduct.application;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.domain.Content;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.dto.response.StudyProductContentReadResponse;
import com.jxx.xuni.studyproduct.dto.response.StudyProductReadResponse;
import com.jxx.xuni.studyproduct.query.StudyProductReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyProductReadService {

    private final StudyProductReadRepository studyProductReadRepository;

    public List<StudyProductReadResponse> readAll() {
        List<StudyProduct> studyProducts = studyProductReadRepository.findAll();

        return studyProducts.stream().map(sp -> new StudyProductReadResponse(
                sp.getName(),
                sp.getCategory(),
                sp.getCreator(),
                sp.getThumbnail())).toList();
    }

    public List<StudyProductReadResponse> readBy(Category category) {
        List<StudyProduct> studyProducts = studyProductReadRepository.findStudyProductByCategory(category);

        return studyProducts.stream().map(sp -> new StudyProductReadResponse(
                sp.getName(),
                sp.getCategory(),
                sp.getCreator(),
                sp.getThumbnail())).toList();
    }

    public StudyProductContentReadResponse readContent(String studyProductId) {
        StudyProduct studyProduct = studyProductReadRepository.readWithContentFetch(studyProductId);
        List<Content> contents = studyProduct.getContents();

        return new StudyProductContentReadResponse(
                studyProduct.getName(),
                studyProduct.getCategory(),
                studyProduct.getCreator(),
                studyProduct.getThumbnail(),
                contents);
    }
}