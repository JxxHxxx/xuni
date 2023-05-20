package com.jxx.xuni.group.query;

import com.jxx.xuni.studyproduct.domain.Category;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;

import java.util.List;

import static com.jxx.xuni.group.domain.GroupStatus.*;
import static com.jxx.xuni.group.domain.QGroup.group;
import static com.jxx.xuni.group.domain.QGroupMember.*;

public class GroupQueryImpl implements GroupQuery {

    private final JPAQueryFactory queryFactory;

    public GroupQueryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<GroupAllQueryResponse> searchGroup(GroupSearchCondition condition, Pageable pageable) {
        List<GroupAllQueryResponse> content = queryFactory
                .select(new QGroupAllQueryResponse(
                        group.id.as("groupId"),
                        group.capacity,
                        group.groupStatus,
                        group.host,
                        group.study,
                        group.time,
                        group.period
                ))
                .from(group)
                .where(
                        categoryEqual(condition.getCategory()),
                        readTypeCond(condition.getReadType())
                )
                .offset(pageable.getOffset())
                .limit(QueryPolicy.LIMIT_OF_PAGE)
                .orderBy(searchGroupOrderSpec(condition))
                .fetch();

        long total = queryFactory
                .selectFrom(group)
                .where(
                        categoryEqual(condition.getCategory()),
                        readTypeCond(condition.getReadType())
                )
                .fetchCount();

        return new PageImpl(content, pageable, total);
    }

    private OrderSpecifier searchGroupOrderSpec(GroupSearchCondition condition) {
        Order direction = condition.isAsc() ? Order.ASC : Order.DESC;
        if (condition.getSortProperty() != null && !condition.getSortProperty().isBlank()) {

            switch (condition.getSortProperty()) {
                case "start-date":
                    return new OrderSpecifier(direction, group.period.startDate);
                case "created-date":
                    return new OrderSpecifier(direction, group.createdDate);
            }
        }

        return new OrderSpecifier(direction, group.period.startDate);
    }

    private BooleanExpression categoryEqual(Category category) {
        return category != null && category.isNotEmpty() ? group.study.category.eq(category) : null;
    }

    private BooleanExpression readTypeCond(String readType) {
        if (QueryUtils.isValid(readType)) {
            if ("default".equals(readType)) {
                return group.groupStatus.in(GATHERING, GATHER_COMPLETE, START);
            }

            if ("gathering".equals(readType)) {
                return group.groupStatus.in(GATHERING);
            }

            if ("all".equals(readType)) {
                return null;
            }
        }
        // 클라이언트가 위 조건과 다른 표현을 사용할 경우 아래 조건이 적용된다.
        return group.groupStatus.in(GATHERING, GATHER_COMPLETE, START);
    }

    @Override
    public List<GroupAllQueryResponse> readOwnWithFetch(Long groupMemberId) {
        return queryFactory
                .select(new QGroupAllQueryResponse(
                        group.id.as("groupId"),
                        group.capacity,
                        group.groupStatus,
                        group.host,
                        group.study,
                        group.time,
                        group.period
                ))
                .from(group)
                .leftJoin(group.groupMembers, groupMember)
                .where(
                        groupMember.groupMemberId.eq(groupMemberId),
                        groupMember.isLeft.eq(false)
                )
                .orderBy(readOwnOrderSpec())
                .fetch();
    }

    private OrderSpecifier readOwnOrderSpec() {
        Order direction = Order.DESC;
        return new OrderSpecifier(direction, groupMember.lastVisitedTime);
    }
}
