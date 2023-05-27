package com.jxx.xuni.studyproduct.presentation;

import com.jxx.xuni.auth.presentation.Admin;
import com.jxx.xuni.common.service.AmazonS3Handler;
import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
import com.jxx.xuni.studyproduct.dto.request.StudyProductContentForm;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import com.jxx.xuni.studyproduct.dto.response.StudyProductApiResult;
import com.jxx.xuni.studyproduct.dto.response.StudyProductApiSimpleResult;
import com.jxx.xuni.studyproduct.dto.response.StudyProductCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_CREATED;

@RestController
@RequiredArgsConstructor
public class StudyProductCreateController {

    private final StudyProductCreateService studyProductCreateService;
    private final AmazonS3Handler amazonS3Handler;

    @Value("${cloud.aws.s3.image.dns}")
    private String s3ImageOrigin;

    @Admin
    @PostMapping("/study-products")
    public ResponseEntity<StudyProductApiResult> createStudyProduct(@RequestPart(value = "image", required = false) MultipartFile file,
                                                                    @RequestPart("data") StudyProductForm form) throws IOException {

        String objectKey = amazonS3Handler.putS3Object(file);
        StudyProductCreateResponse response = studyProductCreateService.create(form, s3ImageOrigin + objectKey);

        return new ResponseEntity(new StudyProductApiResult<>(201, STUDY_PRODUCT_CREATED, response), HttpStatus.CREATED);
    }

    @Admin
    @PostMapping("/study-products/{study-product-id}")
    public ResponseEntity<StudyProductApiSimpleResult> createStudyProductContent(@PathVariable("study-product-id") String studyProductId,
                                                                                 @RequestBody List<StudyProductContentForm> StudyProductDetailForms) {

        studyProductCreateService.putContent(studyProductId, StudyProductDetailForms);
        return new ResponseEntity(StudyProductApiSimpleResult.createDetail(), HttpStatus.CREATED);
    }
}