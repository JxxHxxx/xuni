package com.jxx.xuni.group.query.dynamic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConditionUtils {

    public static boolean isValid(String condition) {
        return condition != null && !condition.isBlank();
    }
}