package com.jxx.xuni.statistics.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProductStatisticsRepository extends JpaRepository<StudyProductStatistics, String> {

    Page<StudyProductStatistics> readBy(Pageable pageable);
}