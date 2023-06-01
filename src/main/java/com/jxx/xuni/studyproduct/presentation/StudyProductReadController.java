package com.jxx.xuni.studyproduct.presentation;

import com.jxx.xuni.studyproduct.application.StudyProductReadService;
import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.dto.response.StudyProductApiReadResult;
import com.jxx.xuni.studyproduct.dto.response.StudyProductContentReadResponse;
import com.jxx.xuni.studyproduct.dto.response.StudyProductReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_DETAIL_READ;
import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_READ;


@RestController
@RequiredArgsConstructor
public class StudyProductReadController {

    private final StudyProductReadService studyProductReadService;

    @GetMapping("/study-products")
    public ResponseEntity<StudyProductApiReadResult> readAll(@RequestParam(required = false, defaultValue = "0") int page,
                                                             @RequestParam(required = false, defaultValue = "10") int size) {
        List<StudyProductReadResponse> responses = studyProductReadService.readBy(PageRequest.of(page, size));

        return ResponseEntity.ok(new StudyProductApiReadResult(STUDY_PRODUCT_READ, responses));
    }

    @GetMapping("/study-products/cond")
    public ResponseEntity<StudyProductApiReadResult> readAllBy(@RequestParam Category category) {
        List<StudyProductReadResponse> responses = studyProductReadService.readBy(category);

        return ResponseEntity.ok(new StudyProductApiReadResult(category.name() + " " + STUDY_PRODUCT_READ, responses));
    }

    @GetMapping("/study-products/{study-product-id}")
    public ResponseEntity<StudyProductApiReadResult> readOne(@PathVariable("study-product-id") String studyProductId) {
        StudyProductContentReadResponse response = studyProductReadService.readContent(studyProductId);

        return ResponseEntity.ok(new StudyProductApiReadResult(STUDY_PRODUCT_DETAIL_READ, response));
    }
}