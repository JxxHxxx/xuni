package com.jxx.xuni.group.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Capacity {
    protected static final Integer CAPACITY_MAX = 20;
    protected static final Integer CAPACITY_MIN = 1;

    private Integer totalCapacity;
    private Integer leftCapacity;

    public Capacity(Integer capacity) {
        this.totalCapacity = capacity;
        this.leftCapacity = capacity;
    }
    protected void subtractOneLeftCapacity() {
        this.leftCapacity -= 1;
    }

    protected void addOneLeftCapacity() {
        this.leftCapacity += 1;
    }

    protected boolean isNotLeftCapacity() {
        return this.leftCapacity <= 0;
    }
}
