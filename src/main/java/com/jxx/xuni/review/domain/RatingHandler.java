package com.jxx.xuni.review.domain;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RatingHandler {

    public double calculateAvg(List<Review> reviews) {
        List<Byte> ratings = reviews.stream().map(r -> r.receiveRating()).toList();
        Integer totalRating = 0;
        for (Byte rating : ratings) {
            totalRating += rating;
        }

        return BigDecimal.valueOf(totalRating / (double) ratings.size())
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}