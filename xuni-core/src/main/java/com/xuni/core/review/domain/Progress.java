package com.xuni.core.review.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum Progress {
    ZERO(0), LIGHTLY(1),  HALF(50), ALMOST(90);

    private int percent;

    Progress(int percent) {
        this.percent = percent;
    }

    public static Progress rate(int progress) {
        if (progress >= 90) {
            return ALMOST;
        }
        if (progress >= 50) {
            return HALF;
        }
        if (progress >= 1) {
            return LIGHTLY;
        }

        return ZERO;
    }

}
