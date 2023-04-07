package com.jxx.xuni.studyproduct.presentation;

import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.Topic;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyProductCreateController {

    private final StudyProductCreateService studyProductCreateService;

    @PostMapping("/study-products")
    public String enroll(@RequestBody StudyProductForm form) {
        StudyProduct studyProduct = new StudyProduct(form.name(),form.category(), Topic.of(form.content(), form.author(), form.image()));
        studyProductCreateService.create(studyProduct);

        return "생성 완료";
    }
}