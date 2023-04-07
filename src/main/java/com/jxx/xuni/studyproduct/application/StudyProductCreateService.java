package com.jxx.xuni.studyproduct.application;

import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyProductCreateService {

    private final StudyProductRepository studyProductRepository;

    public void create(StudyProduct studyProduct) {
        studyProductRepository.save(studyProduct);
    }
}