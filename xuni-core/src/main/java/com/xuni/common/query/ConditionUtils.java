package com.xuni.common.query;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConditionUtils {

    public static boolean isNotNullAndBlank(String condition) {
        return condition != null && !condition.isBlank();
    }
}