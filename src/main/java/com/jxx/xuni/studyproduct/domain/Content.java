package com.jxx.xuni.studyproduct.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    private Long chapterId;
    private String title;

    public Content(Long chapterId, String title) {
        this.chapterId = chapterId;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content detail = (Content) o;
        return Objects.equals(chapterId, detail.chapterId) && Objects.equals(title, detail.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chapterId, title);
    }
}
