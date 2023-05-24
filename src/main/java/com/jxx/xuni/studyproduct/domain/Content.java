package com.jxx.xuni.studyproduct.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    private Long chapterNo;
    private String title;

    public Content(Long chapterNo, String title) {
        this.chapterNo = chapterNo;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content detail = (Content) o;
        return Objects.equals(chapterNo, detail.chapterNo) && Objects.equals(title, detail.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chapterNo, title);
    }
}
