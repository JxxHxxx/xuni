package com.jxx.xuni.studyproduct.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;
import static com.jxx.xuni.studyproduct.dto.response.StudyProductExceptionMessage.NOT_EXIST_CATEGORY;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudyProductReadExceptionTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("클라이언트가 존재하지 않는 상품 식별자로 READ 요청을 보낼 경우" +
            "400 상태 코드와 함께 {NOT_EXIST_ENTITY} 메시지를 응답한다.")
    @Test
    void read_content_exp_cause_not_exist_id() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/study-products/{study-product-id}","not-exist-study-product-id"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(NOT_EXIST_ENTITY));
    }

    @DisplayName("클라이언트가 존재하지 않는 상품 카테고리로 READ 요청을 보낼 경우" +
            "400 상태 코드와 함께 {NOT_EXIST_CATEGORY} 메시지를 응답한다.")
    @Test
    void read_by_category_exp_cause_not_exist_category() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/study-products/cond")
                        .param("category", "JAVAAAA")) //존재하지 않는 category
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(NOT_EXIST_CATEGORY));
    }
}
