package com.jxx.xuni.group.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.jxx.xuni.group.domain.exception.GroupExceptionMessage.INCORRECT_PERIOD;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {
    private LocalDate startDate;
    private LocalDate endDate;

    private Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period of(LocalDate startDate, LocalDate endDate) {
        return new Period(startDate, endDate);
    }

    protected void verifyPeriod() {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(INCORRECT_PERIOD);
        };
    }
}
