package com.jxx.xuni.group.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyCheck {
    private Long memberId;
    private Long chapterId;
    private String title;
    private boolean isDone;

    private StudyCheck(Long memberId, Long chapterId, String title, boolean isDone) {
        this.memberId = memberId;
        this.chapterId = chapterId;
        this.title = title;
        this.isDone = isDone;
    }

    public static StudyCheck init(Long memberId, Long chapterId, String title) {
        return new StudyCheck(memberId, chapterId, title, false);
    }
}
