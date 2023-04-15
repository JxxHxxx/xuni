package com.jxx.xuni.group.domain;

import com.jxx.xuni.studyproduct.domain.Category;
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
public class Study {

    @Column(name = "study_product_id")
    private String id;
    private String subject;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Study(String id, String subject, Category category) {
        this.id = id;
        this.subject = subject;
        this.category = category;
    }

    public static Study of(String id, String subject, Category category) {
        return new Study(id, subject, category);
    }

}
