package com.xuni.api.review.presentation;

import com.xuni.api.review.application.ReviewLikeService;
import com.xuni.api.review.application.ReviewService;
import com.xuni.api.review.infra.ReviewLikeRepository;
import com.xuni.api.review.infra.ReviewRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class ReviewControllerTestConfig {
    @MockBean
    ReviewRepository reviewRepository;
    @MockBean
    ReviewService reviewService;
    @MockBean
    ReviewLikeService reviewLikeService;
    @MockBean
    ReviewLikeRepository reviewLikeRepository;

}
