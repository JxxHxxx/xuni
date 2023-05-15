package com.jxx.xuni.group.query;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryUtils {

    public static boolean isValid(String condition) {
        return condition != null && !condition.isBlank();
    }
}