package com.jxx.xuni.group.domain;

import com.jxx.xuni.subject.domain.Category;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study {
    private String subject;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Study(String subject, Category category) {
        this.subject = subject;
        this.category = category;
    }

    public static Study of(String subject, Category category) {
        return new Study(subject, category);
    }

}
