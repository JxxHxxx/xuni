package com.jxx.xuni.group.domain;

import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.subject.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 그룹 도메인 규칙을 검증합니다.
 */

class GroupTest {

    @DisplayName("초기화 된 Group 인스턴스의 초기 Group Status 는 GATHERING 입니다.")
    @Test
    void check_initialized_group_status() {
        Group group = makeTestGroup(5);

        Assertions.assertThat(group.getGroupStatus()).isEqualTo(GroupStatus.GATHERING);
    }

    @DisplayName("스터디 그룹의 인원은 최소 1인에서 최대 5인 까지 가능합니다. " +
            "이외의 값을 입력할 경우 CapacityOutOfBoundException 발생합니다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 6})
    void check_group_capacity(Integer capacity) {
        Group group = makeTestGroup(capacity);
        Assertions.assertThatThrownBy(() -> group.checkCapacityRange()).isInstanceOf(CapacityOutOfBoundException.class);
    }

    private static Group makeTestGroup(Integer capacity) {
        Group group = new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(capacity),
                Study.of("자바의 정석", Category.JAVA),
                new Host(1l, "재헌"));

        return group;
    }
}