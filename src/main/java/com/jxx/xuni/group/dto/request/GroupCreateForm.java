package com.jxx.xuni.group.dto.request;

import com.jxx.xuni.common.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class GroupCreateForm {
    @NotNull(message = "시작일을 지정하십시오")
    private final LocalDate startDate;
    @NotNull(message = "종료일을 지정하십시오")
    private final LocalDate endDate;
    @NotNull(message = "시작 시간을 지정하십시오")
    private final LocalTime startTime;
    @NotNull(message = "종료 시간을 지정하십시오")
    private final LocalTime endTime;
    @NotNull(message = "스터디 그룹 총원을 지정하십시오")
    private final Integer capacity;
    @NotBlank(message = "스터디 주제를 선택하지 않았습니다")
    private final String studyProductId;
    @NotBlank(message = "스터디 주제를 선택하지 않았습니다")
    private final String subject;
    @NotNull(message = "스터디 주제를 선택하지 않았습니다")
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
