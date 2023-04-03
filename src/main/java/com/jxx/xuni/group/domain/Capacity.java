package com.jxx.xuni.group.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Capacity {
    protected static final Integer CAPACITY_MAX = 5;
    protected static final Integer CAPACITY_MIN = 1;

    private Integer totalCapacity;
    private Integer leftCapacity;

    public Capacity(Integer capacity) {
        this.totalCapacity = capacity;
        this.leftCapacity = capacity;
    }

    protected boolean isValidLeft() {
        return this.leftCapacity > 0;
    }
}
