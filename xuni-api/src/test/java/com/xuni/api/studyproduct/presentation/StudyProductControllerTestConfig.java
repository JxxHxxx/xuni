package com.xuni.api.studyproduct.presentation;

import com.xuni.api.common.service.AmazonS3Handler;
import com.xuni.common.query.PageConverter;
import com.xuni.api.studyproduct.application.StudyProductCreateService;
import com.xuni.api.studyproduct.application.StudyProductReadService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class StudyProductControllerTestConfig {
    @MockBean
    StudyProductCreateService studyProductCreateService;
    @MockBean
    StudyProductReadService studyProductReadService;
    @MockBean
    PageConverter pageConverter;
    @MockBean
    AmazonS3Handler amazonS3Handler;
}
