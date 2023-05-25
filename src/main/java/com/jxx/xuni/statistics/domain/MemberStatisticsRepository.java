package com.jxx.xuni.statistics.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberStatisticsRepository extends JpaRepository<MemberStatistics, Long> {

    @Query(value = "select ms from MemberStatistics ms " +
            "where ms.memberId =:memberId " +
            "and ms.studyProductId =:studyProductId")
    Optional<MemberStatistics> readBy(@Param("memberId") Long memberId, @Param("studyProductId") String studyProductId);
}
