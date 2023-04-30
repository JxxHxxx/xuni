package com.jxx.xuni.studyproduct.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AdminMember;
import com.jxx.xuni.common.service.AmazonS3Handler;
import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
import com.jxx.xuni.studyproduct.dto.request.StudyProductDetailForm;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import com.jxx.xuni.studyproduct.dto.response.StudyProductApiSimpleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyProductCreateController {

    private final StudyProductCreateService studyProductCreateService;
    private final AmazonS3Handler amazonS3Handler;

    @Value("${cloud.aws.s3.image.dns}")
    private String s3ImageOrigin;

    @PostMapping("/study-products")
    public ResponseEntity<StudyProductApiSimpleResult> enroll(@AdminMember MemberDetails memberDetails,
                           @RequestPart(value = "image", required = false) MultipartFile file,
                           @RequestPart("data") StudyProductForm form) throws IOException {

        String objectKey = amazonS3Handler.putS3Object(file);
        studyProductCreateService.create(form, s3ImageOrigin + objectKey);

        return new ResponseEntity(StudyProductApiSimpleResult.create(), HttpStatus.CREATED);

    }

    @PostMapping("/study-products/{study-product-id}")
    public ResponseEntity<StudyProductApiSimpleResult> createDetail(@AdminMember MemberDetails memberDetails,
                                                                     @PathVariable("study-product-id") String studyProductId,
                                                                     @RequestBody List<StudyProductDetailForm> StudyProductDetailForms) {

        studyProductCreateService.createDetail(studyProductId, StudyProductDetailForms);
        return new ResponseEntity(StudyProductApiSimpleResult.createDetail(), HttpStatus.CREATED);
    }
}