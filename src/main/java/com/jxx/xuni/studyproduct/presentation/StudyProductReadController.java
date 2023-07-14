package com.jxx.xuni.studyproduct.presentation;

import com.jxx.xuni.group.dto.response.PageInfo;
import com.jxx.xuni.group.query.converter.PageConverter;
import com.jxx.xuni.studyproduct.application.StudyProductReadService;
import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.dto.response.*;
import com.jxx.xuni.studyproduct.query.dynamic.StudyProductSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.*;


@RestController
@RequiredArgsConstructor
public class StudyProductReadController {

    private final StudyProductReadService studyProductReadService;
    private final PageConverter pageConverter;

    @GetMapping("/study-products")
    public ResponseEntity<StudyProductApiReadResult> readMany(@RequestParam(required = false, defaultValue = "0") int page,
                                                              @RequestParam(required = false, defaultValue = "10") int size) {
        List<StudyProductReadResponse> responses = studyProductReadService.readMany(PageRequest.of(page, size));

        return ResponseEntity.ok(new StudyProductApiReadResult(STUDY_PRODUCT_READ, responses));
    }

    @GetMapping("/study-products/cond")
    public ResponseEntity<StudyProductApiReadResult> readManyBy(@RequestParam(defaultValue = "JAVA") Category category) {
        List<StudyProductReadResponse> responses = studyProductReadService.readManyBy(category);

        return ResponseEntity.ok(new StudyProductApiReadResult(category.name() + " " + STUDY_PRODUCT_READ, responses));
    }

    // TODO : 상품 Content 가 존재하지 않을 때 조회할 수 없는 현상
    @GetMapping("/study-products/{study-product-id}")
    public ResponseEntity<StudyProductApiReadResult> readOne(@PathVariable("study-product-id") String studyProductId) {
        StudyProductContentReadResponse response = studyProductReadService.readDetailBy(studyProductId);

        return ResponseEntity.ok(new StudyProductApiReadResult(STUDY_PRODUCT_DETAIL_READ, response));
    }

    @GetMapping("/study-products/search")
    public ResponseEntity<StudyProductPageApiResult> search(@ModelAttribute StudyProductSearchCondition condition,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size) {

        Page<StudyProductQueryResponse> pageResponse = studyProductReadService.searchStudyProduct(condition, page, size);
        List<StudyProductQueryResponse> contents = pageResponse.getContent();
        PageInfo pageInfo = pageConverter.toPageInto(pageResponse);
        return ResponseEntity.ok(new StudyProductPageApiResult(SEARCH_STUDY_PRODUCT_COND, contents, pageInfo));
    }
}