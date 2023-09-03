package com.xuni.core.statistics;

import com.xuni.core.statistics.domain.MemberStatistics;

public class MemberStatisticsSupporter {
    public static MemberStatistics getBasicMemberStatistics() {
        MemberStatistics statistics = MemberStatistics.builder()
                .memberId(1l)
                .studyProductId("study-product-identifier")
                .progress(10).build();

        return statistics;
    }
}
