package com.jxx.xuni.group.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyCheck {
    @Column(name = "group_member_id")
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

    public void check() {
        if (!isDone) {
            isDone = true;
            return;
        }

        if (isDone) {
            isDone = false;
        }
    }

    public boolean isSameChapter(Long chapterId) {
        return this.chapterId.equals(chapterId);
    }

    public boolean isSameMember(Long memberId) {
        return this.memberId.equals(memberId);
    }
}
