package com.xuni.api.review.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuni.auth.application.SimpleMemberDetails;
import com.xuni.auth.domain.AuthProvider;
import com.xuni.auth.domain.LoginInfo;
import com.xuni.auth.domain.Member;
import com.xuni.auth.domain.MemberRepository;
import com.xuni.auth.support.JwtTokenProvider;
import com.xuni.common.domain.Category;
import com.xuni.review.dto.request.ReviewForm;
import com.xuni.studyproduct.domain.StudyProduct;
import com.xuni.studyproduct.domain.StudyProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewApiExceptionTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudyProductRepository studyProductRepository;

    Long memberId;
    String studyProductId;
    String accessToken;

    @BeforeEach
    void beforeEach() {
        Member member = memberRepository.save(new Member(
                LoginInfo.of("xuni@xuni.com", "0000000"),
                "유니",
                AuthProvider.XUNI));

        StudyProduct savedStudyProduct = studyProductRepository.save(StudyProduct.builder()
                .creator("creator")
                .name("study-product-name")
                .category(Category.JAVA)
                .build());

        memberId = member.getId();
        studyProductId = savedStudyProduct.getId();
        accessToken = jwtTokenProvider.issue(new SimpleMemberDetails(memberId, "xuni@xuni.com", "유니"));
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @DisplayName("클라이언트 측에서 5점 이상의 평점을 입력할 경우 " +
            "400 상태 코드를 응답하며" +
            "응답 message '0 - 5 사이의 점수를 매겨주세요' 를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 6})
    void review_create_fail_cuz_rating_out_range(Integer rating) throws Exception {
        ReviewForm reviewForm = new ReviewForm(
                rating, // 평점을 의미하는 파라미터, 범위는 0 부터 5 입니다.
                "Java 기본기를 닦을 수 있어 좋았습니다.",
                50);

        mockMvc.perform(post("/study-products/{study-product-id}/reviews", studyProductId)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("0 - 5 사이의 점수를 매겨주세요"));
    }

    @DisplayName("클라이언트 측에서 5점 이상의 평점을 입력할 경우 " +
            "400 상태 코드를 응답하며" +
            "응답 message '0 - 5 사이의 점수를 매겨주세요' 를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"열글자 미만", ReviewConst.SIZE_OVER_500_STRINGS})
    void review_create_fail_cuz_comment_out_range(String comment) throws Exception {
        ReviewForm reviewForm = new ReviewForm(
                5,
                comment, // 리뷰는 10 자 이상 500자 이하
                50);

        mockMvc.perform(post("/study-products/{study-product-id}/reviews", studyProductId)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("10자 이상 작성해주세요. 500자 까지"));
    }

    @DisplayName("클라이언트 측에서 5점 이상의 평점을 입력할 경우 " +
            "400 상태 코드를 응답하며" +
            "응답 message 알 수 없는 에러가 발생했습니다' 를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void review_create_fail_cuz_progress_out_range(Integer progress) throws Exception {
        ReviewForm reviewForm = new ReviewForm(
                5,
                "Java 기본기를 닦을 수 있어 좋았습니다.",
                progress); // 진행률을 의미하며 범위는 0 부터 100 입니다.

        mockMvc.perform(post("/study-products/{study-product-id}/reviews", studyProductId)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("알 수 없는 에러가 발생했습니다"));
    }

    @DisplayName("rating 파라미터 null 체크")
    @ParameterizedTest
    @NullSource
    void review_create_fail_cuz_rating_is_null(Integer rating) throws Exception {
        ReviewForm reviewForm = new ReviewForm(
                rating,
                "Java 기본기를 닦을 수 있어 좋았습니다.",
                50);

        mockMvc.perform(post("/study-products/{study-product-id}/reviews", studyProductId)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("null 일 수 없습니다"));
    }

    @DisplayName("rating 파라미터 null 체크")
    @ParameterizedTest
    @NullSource
    void review_create_fail_cuz_comment_is_null(String comment) throws Exception {
        ReviewForm reviewForm = new ReviewForm(
                5,
                comment,
                50);

        mockMvc.perform(post("/study-products/{study-product-id}/reviews", studyProductId)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("공백일 수 없습니다"));
    }

    @DisplayName("rating 파라미터 null 체크")
    @ParameterizedTest
    @NullSource
    void review_create_fail_cuz_progress_is_null(Integer progress) throws Exception {
        ReviewForm reviewForm = new ReviewForm(
                5,
                "Java 기본기를 닦을 수 있어 좋았습니다.",
                progress);

        mockMvc.perform(post("/study-products/{study-product-id}/reviews", studyProductId)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("null 일 수 없습니다"));
    }
}
