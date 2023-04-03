package com.jxx.xuni.group.dto.request;

import com.jxx.xuni.subject.domain.Category;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class GroupCreateForm {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer capacity;
    private String subject;
    private Category category;

    public GroupCreateForm(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer capacity, String subject, Category category) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.subject = subject;
        this.category = category;
    }
}
