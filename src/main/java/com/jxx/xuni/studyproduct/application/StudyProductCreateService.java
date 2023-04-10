package com.jxx.xuni.studyproduct.application;

import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import com.jxx.xuni.studyproduct.domain.Topic;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyProductCreateService {

    private final StudyProductRepository studyProductRepository;

    public void create(StudyProductForm form) {
        StudyProduct studyProduct = new StudyProduct(form.name(),form.category(), Topic.of(form.content(), form.author(), form.image()));
        studyProductRepository.save(studyProduct);
    }
}