package com.jxx.xuni.group.domain;

import com.jxx.xuni.subject.domain.Category;
import java.time.LocalDate;
import java.time.LocalTime;

public class GroupCreator {

    public static Group receiveBasicSample() {
        return new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of("자바의 정석", Category.JAVA),
                new Host(1l, "재헌"));
    }
}
