package com.jxx.xuni.studyproduct.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AdminMember;
import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
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
    public String enroll(@AdminMember MemberDetails memberDetails, @RequestBody StudyProductForm form) {
        studyProductCreateService.create(form);
        return "스터디 상품 등록 완료";
    }
}