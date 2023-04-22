package com.jxx.xuni.group.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query(value = "select g from Group g where g.id = :groupId")
    Optional<Group>  readWithOptimisticLock(@Param("groupId") Long groupId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE study_group g SET g.group_status = 'START' " +
            "WHERE g.start_date = curdate() " +
            "AND (g.group_status = 'GATHERING' OR g.group_status = 'GATHER_COMPLETE')",
            nativeQuery = true)
    void updateGroupStatusToStart();

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE study_group g SET g.group_status = 'END' " +
            "WHERE g.end_date <= DATE_SUB(CURDATE(), INTERVAL 3 DAY) " +
            "AND g.group_status = 'START'",
            nativeQuery = true)
    void updateGroupStatusToEnd();
}
