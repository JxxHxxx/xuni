package com.xuni.api.statistics.infra;

import com.xuni.core.statistics.domain.StudyProductStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProductStatisticsRepository extends JpaRepository<StudyProductStatistics, String> {

    Page<StudyProductStatistics> readBy(Pageable pageable);
}