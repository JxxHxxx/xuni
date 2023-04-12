package com.jxx.xuni.group.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query(value = "select g from Group g where g.id = :groupId")
    Optional<Group>  readWithOptimisticLock(@Param("groupId") Long groupId);
}
