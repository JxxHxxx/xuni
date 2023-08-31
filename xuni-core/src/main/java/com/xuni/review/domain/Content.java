package com.xuni.review.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {
    private Integer rating;
    private String comment;

    private Content(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public static Content of(Integer rating, String comment) {
        return new Content(rating, comment);
    }

    protected void update(Integer rating, String comment) {
        if (rating != null) {
            this.rating = rating;
        }

        if (comment != null) {
            if (!comment.isBlank()) {
                this.comment = comment;
            }
        }
    }
}