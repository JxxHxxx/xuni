package com.jxx.xuni.studyproduct.presentation;

import com.jxx.xuni.studyproduct.application.StudyProductReadService;
import com.jxx.xuni.studyproduct.domain.Category;
import com.jxx.xuni.studyproduct.dto.response.StudyProductApiReadResult;
import com.jxx.xuni.studyproduct.dto.response.StudyProductReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyProductReadController {

    private final StudyProductReadService studyProductReadService;

    @GetMapping("/study-products")
    public ResponseEntity<StudyProductApiReadResult> readAll() {
        List<StudyProductReadResponse> responses = studyProductReadService.readAll();

        return ResponseEntity.ok(new StudyProductApiReadResult("전체 조회 완료",responses));
    }

    @GetMapping("/study-products")
    public ResponseEntity<StudyProductApiReadResult> readAllBy(@RequestParam Category category) {
        List<StudyProductReadResponse> responses = studyProductReadService.readBy(category);

        return ResponseEntity.ok(new StudyProductApiReadResult(category.name() + " 전체 조회 완료", responses));
    }
}