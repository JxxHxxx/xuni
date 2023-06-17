package com.jxx.xuni.group.dto.request;

import com.jxx.xuni.common.domain.Category;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class GroupCreateForm {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Integer capacity;
    private final String studyProductId;
    private final String subject;
    private final Category category;

    public GroupCreateForm(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer capacity, String studyProductId, String subject, Category category) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.studyProductId = studyProductId;
        this.subject = subject;
        this.category = category;
    }
}
