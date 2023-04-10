package com.jxx.xuni.studyproduct.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.group.domain.TestGroupServiceSupporter;
import com.jxx.xuni.studyproduct.StudyProductControllerTestConfig;
import com.jxx.xuni.studyproduct.domain.Category;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudyProductCreateControllerTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    String requestForm;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        StudyProductForm studyProductForm = new StudyProductForm("JAVA 스터디", Category.JAVA, "JAVA의 정석", "남궁성", "URL");
        requestForm = objectMapper.writeValueAsString(studyProductForm);
    }


    @DisplayName("상품 등록 성공")
    @Test
    void enroll_success() throws Exception {
        //given
        String AdminToken = jwtTokenProvider.issue(TestGroupServiceSupporter.AdminMemberDetails(1l));

        //when - then
        mockMvc.perform(MockMvcRequestBuilders.post("/study-products")
                        .header("Authorization", AdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestForm))
                .andExpect(content().string("스터디 상품 등록 완료"));
    }


    @DisplayName("상품 등록 실패")
    @Test
    void enroll_fail() throws Exception {
        //given
        String userToken = jwtTokenProvider.issue(TestGroupServiceSupporter.UserMemberDetails(1l));

        //when - then
        mockMvc.perform(MockMvcRequestBuilders.post("/study-products")
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestForm))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("권한이 존재하지 않습니다."));

    }
}