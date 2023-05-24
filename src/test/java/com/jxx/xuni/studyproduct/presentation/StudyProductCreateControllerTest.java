package com.jxx.xuni.studyproduct.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.common.service.AmazonS3Handler;
import com.jxx.xuni.group.domain.TestGroupServiceSupporter;
import com.jxx.xuni.studyproduct.application.StudyProductCreateService;
import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.dto.request.StudyProductContentForm;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.jxx.xuni.ApiDocumentUtils.*;
import static com.jxx.xuni.common.exception.CommonExceptionMessage.ONLY_ADMIN;
import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_CREATED;
import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_DETAIL_CREATED;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(StudyProductControllerTestConfig.class)
class StudyProductCreateControllerTest extends StudyProductCommon{
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AmazonS3Handler amazonS3Handler;
    @Autowired
    StudyProductCreateService studyProductCreateService;
    byte[] byteForm;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        StudyProductForm studyProductForm = new StudyProductForm("JAVA의 정석",Category.JAVA, "남궁성");
        byteForm = objectMapper.writeValueAsBytes(studyProductForm);
    }

    @DisplayName("상품 등록 성공")
    @Test
    void study_product_enroll_success() throws Exception {
        //given
        String AdminToken = jwtTokenProvider.issue(TestGroupServiceSupporter.AdminMemberDetails(1l));
        MockMultipartFile data = new MockMultipartFile("data", "data", "application/json", byteForm);

        ResultActions result = mockMvc.perform(multipart("/study-products")
                .file(data)
                .header("Authorization", AdminToken));

        result
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(STUDY_PRODUCT_CREATED))

                .andDo(MockMvcRestDocumentation.document("studyproduct/create/self",
                        PayloadDocumentation.requestPartFields("data",
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("스터디 상품 카테고리"),
                                fieldWithPath("creator").type(JsonFieldType.STRING).description("스터디 상품 저자")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }


    @DisplayName("상품 등록 실패 - 관리자 권한(ADMIN)이 없을 경우 " +
            "403 상태코드 " +
            "ONLY_ADMIN 메시지 반환")
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
                .andExpect(jsonPath("$.message").value(ONLY_ADMIN));
    }

    @DisplayName("스터디 상품 상세 등록 성공")
    @Test
    void create_detail_success() throws Exception {
        //given
        String AdminToken = jwtTokenProvider.issue(TestGroupServiceSupporter.AdminMemberDetails(1l));
        String studyProductId = "6f7e5d4a-4893-4efc-b8c4-fdda39cabd55";
        List<StudyProductContentForm> form = List.of(
                new StudyProductContentForm("spring IOC"),
                new StudyProductContentForm("spring AOP"),
                new StudyProductContentForm("spring MVC"));

        //when
        ResultActions result = mockMvc.perform(post("/study-products/{study-product-id}", studyProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .header("Authorization", AdminToken));

        //then
        result
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(STUDY_PRODUCT_DETAIL_CREATED))

                .andDo(MockMvcRestDocumentation.document("studyproduct/create/detail",
                        getDocumentRequest(), getDocumentResponse(),

                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("study-product-id").description("스터디 상품 식별자")
                        ),

                        PayloadDocumentation.requestFields(
                                fieldWithPath("[]title").type(JsonFieldType.STRING).description("스터디 상품 상세 주제")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }
}