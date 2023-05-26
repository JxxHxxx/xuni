package com.jxx.xuni.statistics.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.common.event.trigger.StatisticsUpdateEvent;
import com.jxx.xuni.statistics.application.MemberStatisticsService;
import com.jxx.xuni.statistics.dto.response.MemberStatisticsApiResult;
import com.jxx.xuni.statistics.dto.response.ReviewNeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.jxx.xuni.statistics.dto.response.MemberStatisticsApiMessage.REVIEW_NEED_DATA;

@RestController
@RequiredArgsConstructor
public class MemberStatisticsController {

    private final MemberStatisticsService memberStatisticsService;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/member-statistics/members/{member-id}/study-products/{study-product-id}")
    public ResponseEntity<MemberStatisticsApiResult> sendProgress(@AuthenticatedMember MemberDetails memberDetails,
                                                                  @PathVariable("member-id") Long memberId,
                                                                  @PathVariable("study-product-id") String studyProductId) {

        StatisticsUpdateEvent event = new StatisticsUpdateEvent(memberDetails.getUserId(), studyProductId);
        eventPublisher.publishEvent(event);

        memberDetails.checkPrivateAuthority(memberId);
        ReviewNeedResponse response = memberStatisticsService.findReviewNeedStatistics(memberId, studyProductId);

        return ResponseEntity.ok(new MemberStatisticsApiResult(200, REVIEW_NEED_DATA, response));
    }
}
