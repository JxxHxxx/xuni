package com.xuni.core.group.domain;

import com.xuni.core.group.domain.exception.GroupExceptionMessage;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
            throw new IllegalArgumentException(GroupExceptionMessage.INCORRECT_PERIOD);
        };
    }
}
