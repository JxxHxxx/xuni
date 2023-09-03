package com.xuni.api.group.dto.request;

import com.xuni.core.common.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record GroupCreateForm(
        @NotNull(message = "그룹 이름을 지정하십시오") String name,
        @NotNull(message = "시작일을 지정하십시오") LocalDate startDate,
        @NotNull(message = "종료일을 지정하십시오") LocalDate endDate,
        @NotNull(message = "시작 시간을 지정하십시오") LocalTime startTime,
        @NotNull(message = "종료 시간을 지정하십시오") LocalTime endTime,
        @NotNull(message = "스터디 그룹 총원을 지정하십시오") Integer capacity,
        @NotBlank(message = "스터디 주제를 선택하지 않았습니다") String studyProductId,
        @NotBlank(message = "스터디 주제를 선택하지 않았습니다") String subject,
        @NotNull(message = "스터디 주제를 선택하지 않았습니다") Category category
) {
}

