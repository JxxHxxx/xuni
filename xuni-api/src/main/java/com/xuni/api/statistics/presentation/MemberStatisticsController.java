package com.xuni.api.statistics.presentation;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.presentation.AuthenticatedMember;
import com.xuni.common.http.DataResponse;
import com.xuni.api.statistics.application.MemberStatisticsService;
import com.xuni.api.statistics.dto.response.ReviewNeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.xuni.api.statistics.dto.response.StatisticsApiMessage.REVIEW_NEED_DATA;

@RestController
@RequiredArgsConstructor
public class MemberStatisticsController {

    private final MemberStatisticsService memberStatisticsService;

    @GetMapping("/statistics/members/{member-id}/study-products/{study-product-id}")
    public ResponseEntity<DataResponse> sendProgress(@AuthenticatedMember MemberDetails memberDetails,
                                                     @PathVariable("member-id") Long memberId,
                                                     @PathVariable("study-product-id") String studyProductId) {

        memberDetails.checkPrivateAuthority(memberId);
        ReviewNeedResponse response = memberStatisticsService.readOne(memberId, studyProductId);

        return ResponseEntity.ok(new DataResponse(200, REVIEW_NEED_DATA, response));
    }
}
