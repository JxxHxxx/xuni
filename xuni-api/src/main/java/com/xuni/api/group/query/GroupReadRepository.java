package com.xuni.api.group.query;

import com.xuni.group.domain.Group;
import com.xuni.common.domain.Category;
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
            "where g.id =:groupId")
    Optional<Group> readOneWithFetch(@Param("groupId") Long groupId);

    @Query(value = "select g from Group g " +
            "join fetch g.groupMembers gm " +
            "where g.study.category =:category " +
            "and g.groupStatus <> 'END'")
    List<Group> readByCategoryWithFetch(@Param("category") Category category);

    @Query(value = "select g from Group g " +
            "join fetch g.tasks gt " +
            "where g.id =:groupId " +
            "and gt.memberId =:memberId ")
    Optional<Group> readTaskWithFetch(@Param("groupId") Long groupId, @Param("memberId") Long memberId);

    @Query(value = "select g from Group g " +
            "join fetch g.tasks gt " +
            "where gt.memberId =:memberId " +
            "and g.study.id =:studyProductId")
    Optional<Group> findBy(@Param("memberId") Long memberId, @Param("studyProductId") String studyProductId);
}
