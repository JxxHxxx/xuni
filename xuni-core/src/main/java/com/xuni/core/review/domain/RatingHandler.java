package com.xuni.core.review.domain;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RatingHandler {

    public double calculateAvg(List<Review> reviews) {
        List<Integer> ratings = reviews.stream().map(r -> r.receiveRating()).toList();
        Integer totalRating = 0;
        for (Integer rating : ratings) {
            totalRating += rating;
        }

        return BigDecimal.valueOf(totalRating / (double) ratings.size())
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}