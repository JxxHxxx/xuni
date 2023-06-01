package com.jxx.xuni.studyproduct.application;

import com.jxx.xuni.common.event.trigger.StudyProductCreatedEvent;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.Content;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import com.jxx.xuni.studyproduct.dto.request.StudyProductContentForm;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import com.jxx.xuni.studyproduct.dto.response.StudyProductCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.NOT_EXIST_STUDY_PRODUCT;

@Service
@RequiredArgsConstructor
public class StudyProductCreateService {

    private final StudyProductRepository studyProductRepository;
    private final ApplicationEventPublisher eventPublisher;

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