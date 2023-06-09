package com.jxx.xuni.statistics.presentation;

import com.jxx.xuni.statistics.application.StudyProductStatisticsService;
import com.jxx.xuni.statistics.dto.response.StatisticsApiResult;
import com.jxx.xuni.statistics.dto.response.StudyProductStatisticsReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jxx.xuni.statistics.dto.response.StatisticsApiMessage.STUDY_PRODUCT_STAT_READ_ALL;
import static com.jxx.xuni.statistics.dto.response.StatisticsApiMessage.STUDY_PRODUCT_STAT_READ_ONE;

@RestController
@RequiredArgsConstructor
public class StudyProductStatisticsController {

    private final StudyProductStatisticsService studyProductStatisticsService;

    @GetMapping("/statistics/study-products")
    public ResponseEntity<StatisticsApiResult> readAll(@RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size) {
        List<StudyProductStatisticsReadResponse> response = studyProductStatisticsService.readBy(PageRequest.of(page, size));

        return ResponseEntity.ok(new StatisticsApiResult(200, STUDY_PRODUCT_STAT_READ_ALL, response));
    }

    @GetMapping("/statistics/study-products/{study-product-id}")
    public ResponseEntity<StatisticsApiResult> readOne(@PathVariable("study-product-id") String studyProductId) {
        StudyProductStatisticsReadResponse response = studyProductStatisticsService.readOne(studyProductId);

        return ResponseEntity.ok(new StatisticsApiResult(200,STUDY_PRODUCT_STAT_READ_ONE, response));
    }
}