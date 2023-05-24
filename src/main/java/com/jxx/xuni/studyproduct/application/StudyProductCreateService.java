package com.jxx.xuni.studyproduct.application;

import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.Content;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import com.jxx.xuni.studyproduct.dto.request.StudyProductContentForm;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.NOT_EXIST_STUDY_PRODUCT;

@Service
@RequiredArgsConstructor
public class StudyProductCreateService {

    private final StudyProductRepository studyProductRepository;

    public void create(StudyProductForm form, String imageURL) {
        StudyProduct studyProduct = StudyProduct.builder()
                .name(form.name())
                .creator(form.creator())
                .thumbnail(imageURL)
                .category(form.category())
                .build();

        studyProductRepository.save(studyProduct);
    }

    @Transactional
    public void putContent(String studyProductId, List<StudyProductContentForm> contentForms) {
        Long chapterIdSequence = 1l;
        StudyProduct studyProduct = studyProductRepository.findById(studyProductId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_STUDY_PRODUCT));

        for (StudyProductContentForm form : contentForms) {
            studyProduct.getContents().add(new Content(chapterIdSequence++, form.title()));
        }
    }
}