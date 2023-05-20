package com.jxx.xuni.group.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class TaskTest {

    @DisplayName("studyCheck 객체는 check 메서드를 통해 isDone 필드 값을 변경할 수 있다. " +
            "isDone 필드는 studyCheck 가 초기화되면 false 값을 가진다." +
            "현재 isDone 상태가 false 라면 true 로 변경되고 " +
            "                 true 라면 false 로 변견된다.")
    @Test
    void check() {
        //given
        Task studyCheck = Task.init(1l, 1l, "spring loc");
        Assertions.assertThat(studyCheck.isDone()).isFalse();
        //when
        studyCheck.updateDone();
        //then
        Assertions.assertThat(studyCheck.isDone()).isTrue();

        // 다시 체크 메서드를 호출하면 False 로 변경된다.
        //when
        studyCheck.updateDone();
        //then
        Assertions.assertThat(studyCheck.isDone()).isFalse();
    }
}