package com.jxx.xuni.group.query;

import com.jxx.xuni.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupReadRepository extends JpaRepository<Group, Long> {

    @Query(value = "select g from Group g " +
            "join fetch g.groupMembers gm")
    List<Group> readAllWithFetch();
}
