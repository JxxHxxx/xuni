package com.jxx.xuni.studyproduct.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.group.domain.TestGroupServiceSupporter;
import com.jxx.xuni.studyproduct.domain.Category;
import com.jxx.xuni.studyproduct.dto.request.StudyProductForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("default")
@SpringBootTest
@AutoConfigureMockMvc
class StudyProductCreateControllerTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    byte[] byteForm;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        StudyProductForm studyProductForm = new StudyProductForm("JAVA 스터디", Category.JAVA, "JAVA의 정석", "남궁성");
        byteForm = objectMapper.writeValueAsBytes(studyProductForm);
    }

    @DisplayName("상품 등록 성공")
    @Test
    void enroll_success() throws Exception {
        //given
        String AdminToken = jwtTokenProvider.issue(TestGroupServiceSupporter.AdminMemberDetails(1l));
        MockMultipartFile data = new MockMultipartFile("data", "data", "application/json", byteForm);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/study-products")
                        .file(data)
                        .header("Authorization", AdminToken))
                        .andExpect(content().string("스터디 상품 등록 완료"));
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