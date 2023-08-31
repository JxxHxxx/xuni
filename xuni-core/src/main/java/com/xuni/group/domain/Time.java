package com.xuni.group.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

import static com.xuni.group.domain.exception.GroupExceptionMessage.INCORRECT_TIME;

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
            throw new IllegalArgumentException(INCORRECT_TIME);
        }
    }
}
