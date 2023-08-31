package com.xuni.api.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Range;

public record ReviewForm(
        @NotNull(message = "null 일 수 없습니다")
        @Range(min = 0, max = 5, message = "0 - 5 사이의 점수를 매겨주세요")
        Integer rating,
        @NotBlank(message = "공백일 수 없습니다")
        @Pattern(regexp = "^.{10,500}$", message = "10자 이상 작성해주세요. 500자 까지")
        String comment,
        @NotNull(message = "null 일 수 없습니다")
        /** progress 파라미터는 사용자가 직접 입력하도록 설계 되어 있지 않습니다.
         * progress 파라미터는 프론트 앤드 측의 요청을 통해 입력합니다. MemberStatisticsController
         * uri GET statistics/members/{member-id}/study-products/{study-product-id} 를 통해 progress 를 얻을 수 있습니다.
         *
         * 프론트 측의 실수로 인한 예외 메시지를 감추기 위해 아래와 같은 메시지를 임시로 사용하고 있습니다.
         */
        @Range(min = 0, max = 100, message = "알 수 없는 에러가 발생했습니다")
        Integer progress
) {}
