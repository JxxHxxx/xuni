package com.xuni.core.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reviewer {

    @Column(name = "reviewer_id")
    private Long id;
    @Column(name = "reviewer_name")
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Progress progress;

    private Reviewer(Long id, String name, Progress progress) {
        this.id = id;
        this.name = name;
        this.progress = progress;
    }

    public static Reviewer of(Long id, String name, Progress progress) {
        return new Reviewer(id, name, progress);
    }
}
