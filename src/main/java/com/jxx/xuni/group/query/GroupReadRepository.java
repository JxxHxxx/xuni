package com.jxx.xuni.group.query;

import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.studyproduct.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupReadRepository extends JpaRepository<Group, Long>, GroupQuery {

    @Query(value = "select g from Group g " +
            "join fetch g.groupMembers gm " +
            "where g.groupStatus <> 'END'")
    List<Group> readAllWithFetch();

    @Query(value = "select g from Group g " +
            "join fetch g.groupMembers gm " +
            "where g.study.category =:category " +
            "and g.groupStatus <> 'END'")
    List<Group> readByCategoryWithFetch(@Param("category") Category category);

    @Query(value = "select g from Group g " +
            "join fetch g.studyChecks sc " +
            "where g.id =:groupId " +
            "and sc.memberId =:memberId ")
    Optional<Group> readStudyCheckWithFetch(@Param("groupId") Long groupId, @Param("memberId") Long memberId);
}
