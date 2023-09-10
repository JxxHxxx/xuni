package com.xuni.api.studyproduct.presentation;

import com.xuni.api.auth.annotation.Admin;
import com.xuni.core.common.http.DataResponse;
import com.xuni.core.common.http.SimpleResponse;
import com.xuni.api.common.service.AmazonS3Handler;
import com.xuni.api.studyproduct.application.StudyProductCreateService;
import com.xuni.api.studyproduct.dto.request.StudyProductContentForm;
import com.xuni.api.studyproduct.dto.request.StudyProductForm;
import com.xuni.api.studyproduct.dto.response.StudyProductCreateResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.xuni.api.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_CREATED;
import static com.xuni.api.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_DETAIL_CREATED;

@RestController
@RequiredArgsConstructor
public class StudyProductCreateController {

    private final StudyProductCreateService studyProductCreateService;
    private final AmazonS3Handler amazonS3Handler;

    @Value("${cloud.aws.s3.image.dns}")
    private String s3ImageOrigin;

    @Admin
    @PostMapping("/study-products")
    public ResponseEntity<DataResponse> createStudyProduct(@RequestPart(value = "image", required = false) MultipartFile file,
                                                           @RequestPart("data") StudyProductForm form) throws IOException {

        String objectKey = amazonS3Handler.putS3Object(file);
        StudyProductCreateResponse response = studyProductCreateService.create(form, s3ImageOrigin + objectKey);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DataResponse(HttpStatus.CREATED.value(), STUDY_PRODUCT_CREATED, response));
    }

    @Admin
    @PostMapping("/study-products/{study-product-id}")
    public ResponseEntity<SimpleResponse> createStudyProductContent(@PathVariable("study-product-id") String studyProductId,
                                                                    @RequestBody List<StudyProductContentForm> StudyProductDetailForms) {

        studyProductCreateService.putContent(studyProductId, StudyProductDetailForms);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.LOCATION, "/study-products/" + studyProductId))
                .body(new SimpleResponse(201, STUDY_PRODUCT_DETAIL_CREATED));

    }
}