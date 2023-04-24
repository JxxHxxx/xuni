package com.jxx.xuni.group.query;

import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.studyproduct.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupReadRepository extends JpaRepository<Group, Long> {

    @Query(value = "select g from Group g " +
            "join fetch g.groupMembers gm")
    List<Group> readAllWithFetch();

    @Query(value = "select g from Group g join fetch g.groupMembers gm where g.study.category =:category")
    List<Group> readByCategoryWithFetch(@Param("category") Category category);
}
