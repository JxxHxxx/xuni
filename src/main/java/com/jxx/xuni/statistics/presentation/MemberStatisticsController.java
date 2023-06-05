package com.jxx.xuni.statistics.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.statistics.application.MemberStatisticsService;
import com.jxx.xuni.statistics.dto.response.StatisticsApiResult;
import com.jxx.xuni.statistics.dto.response.ReviewNeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.jxx.xuni.statistics.dto.response.MemberStatisticsApiMessage.REVIEW_NEED_DATA;

@RestController
@RequiredArgsConstructor
public class MemberStatisticsController {

    private final MemberStatisticsService memberStatisticsService;

    @GetMapping("/statistics/members/{member-id}/study-products/{study-product-id}")
    public ResponseEntity<StatisticsApiResult> sendProgress(@AuthenticatedMember MemberDetails memberDetails,
                                                            @PathVariable("member-id") Long memberId,
                                                            @PathVariable("study-product-id") String studyProductId) {

        memberDetails.checkPrivateAuthority(memberId);
        ReviewNeedResponse response = memberStatisticsService.readOne(memberId, studyProductId);

        return ResponseEntity.ok(new StatisticsApiResult(200, REVIEW_NEED_DATA, response));
    }
}
