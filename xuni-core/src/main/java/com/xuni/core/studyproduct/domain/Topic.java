package com.xuni.core.studyproduct.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic {

    private String content;
    private String author;
    private String image;

    private Topic(String content, String author, String image) {
        this.content = content;
        this.author = author;
        this.image = image;
    }

    public static Topic of(String content, String author, String image) {
        return new Topic(content, author, image);
    }
}