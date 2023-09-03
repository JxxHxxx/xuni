package com.xuni.core.group.domain;

import com.xuni.core.group.domain.exception.GroupExceptionMessage;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Embeddable
@NoArgsConstructor
public class Time {

    private LocalTime startTime;
    private LocalTime endTime;

    private Time(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Time of(LocalTime startTime, LocalTime endTime) {
        return new Time(startTime, endTime);
    }

    protected void verifyTime() {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException(GroupExceptionMessage.INCORRECT_TIME);
        }
    }
}
