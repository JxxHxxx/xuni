package com.jxx.xuni.studyproduct.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.common.service.AmazonS3Handler;
import com.jxx.xuni.group.domain.TestGroupServiceSupporter;
import com.jxx.xuni.studyproduct.domain.Category;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.jxx.xuni.ApiDocumentUtils.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(StudyProductControllerTestConfig.class)
class StudyProductCreateControllerTest extends StudyProductCommon{
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AmazonS3Handler amazonS3Handler;
    byte[] byteForm;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        StudyProductForm studyProductForm = new StudyProductForm("JAVA 스터디", Category.JAVA, "JAVA의 정석", "남궁성");
        byteForm = objectMapper.writeValueAsBytes(studyProductForm);
    }

    @DisplayName("상품 등록 성공")
    @Test
    void study_product_enroll_success() throws Exception {
        //given
        String AdminToken = jwtTokenProvider.issue(TestGroupServiceSupporter.AdminMemberDetails(1l));
        MockMultipartFile data = new MockMultipartFile("data", "data", "application/json", byteForm);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.multipart("/study-products")
                .file(data)
                .header("Authorization", AdminToken));

        result.andExpect(content().string("스터디 상품 등록 완료"))
                .andDo(MockMvcRestDocumentation.document("studyproduct/create",
                        getDocumentRequest(),
                        PayloadDocumentation.requestPartFields("data",
                                fieldWithPath("name").type(JsonFieldType.STRING).description("스터디 이름"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("스터디 상품 카테고리"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("author").type(JsonFieldType.STRING).description("스터디 상품 저자")
                        )));
    }


    @DisplayName("상품 등록 실패")
    @Test
    void enroll_fail() throws Exception {
        //given
        String userToken = jwtTokenProvider.issue(TestGroupServiceSupporter.UserMemberDetails(1l));

        //when - then
        MockMultipartFile data = new MockMultipartFile("data", "data", "application/json", byteForm);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/study-products")
                        .file(data)
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("권한이 존재하지 않습니다."));
    }
}