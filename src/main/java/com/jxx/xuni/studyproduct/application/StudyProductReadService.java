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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyProductReadService {

    private final StudyProductReadRepository studyProductReadRepository;

    public List<StudyProductReadResponse> readAll() {
        List<StudyProduct> studyProducts = studyProductReadRepository.findAll();

        return mappedResponse(studyProducts);
    }

    public List<StudyProductReadResponse> readBy(Category category) {
        List<StudyProduct> studyProducts = studyProductReadRepository.findStudyProductByCategory(category);

        return mappedResponse(studyProducts);
    }

    private static List<StudyProductReadResponse> mappedResponse(List<StudyProduct> studyProducts) {
        return studyProducts.stream().map(studyProduct -> new StudyProductReadResponse(
                studyProduct.getName(),
                studyProduct.getCategory(),
                studyProduct.getTopic().getContent(),
                studyProduct.getTopic().getAuthor(),
                studyProduct.getTopic().getImage())).collect(Collectors.toList());
    }

    public StudyProductContentReadResponse readContent(String studyProductId) {
        StudyProduct studyProduct = studyProductReadRepository.readWithContentFetch(studyProductId);
        List<Content> studyProductDetails = studyProduct.getContents();

        return new StudyProductContentReadResponse(
                studyProduct.getName(),
                studyProduct.getCategory(),
                studyProduct.getTopic().getContent(),
                studyProduct.getTopic().getAuthor(),
                studyProduct.getTopic().getImage(),
                studyProductDetails);
    }
}