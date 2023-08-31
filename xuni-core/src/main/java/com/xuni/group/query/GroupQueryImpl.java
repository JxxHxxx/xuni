package com.xuni.group.query;


import com.xuni.common.query.ConditionUtils;
import com.xuni.group.domain.GroupStatus;
import com.xuni.group.domain.QGroup;
import com.xuni.group.domain.QGroupMember;
import com.xuni.group.query.dynamic.GroupSearchCondition;
import com.xuni.common.domain.Category;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;

import java.util.List;

/** Queydsl을 통해 쿼리문을 작성할 시 아래의 규칙에 따라 작성해주십시오.
 * @See <a href="https://github.com/JxxHxxx/xuni-api-server/wiki/coding-convention#method_rules_querydsl_condition" />
 */
public class GroupQueryImpl implements GroupQuery {

    private final JPAQueryFactory queryFactory;

    public GroupQueryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<GroupAllQueryResponse> searchGroup(GroupSearchCondition condition, Pageable pageable) {
        List<GroupAllQueryResponse> content = queryFactory
                .select(new QGroupAllQueryResponse(
                        QGroup.group.id.as("groupId"),
                        QGroup.group.capacity,
                        QGroup.group.groupStatus,
                        QGroup.group.host,
                        QGroup.group.study,
                        QGroup.group.time,
                        QGroup.group.period
                ))
                .from(QGroup.group)
                .where(
                        categoryEqual(condition.getCategory()),
                        readTypeCond(condition.getReadType()),
                        studySubjectContain(condition.getSubject())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(searchGroupOrderSpec(condition))
                .fetch();
        // TODO : 무언가 where 문이 이상한데?
        long total = queryFactory
                .selectFrom(QGroup.group)
                .where(
                        categoryEqual(condition.getCategory()),
                        readTypeCond(condition.getReadType()),
                        studySubjectContain(condition.getSubject())
                )
                .fetchCount();

        return new PageImpl(content, pageable, total);
    }

    private OrderSpecifier searchGroupOrderSpec(GroupSearchCondition condition) {
        Order direction = condition.isAsc() ? Order.ASC : Order.DESC;
        if (condition.getSortProperty() != null && !condition.getSortProperty().isBlank()) {

            switch (condition.getSortProperty()) {
                case "start-date":
                    return new OrderSpecifier(direction, QGroup.group.period.startDate);
                case "created-date":
                    return new OrderSpecifier(direction, QGroup.group.createdDate);
            }
        }

        return new OrderSpecifier(direction, QGroup.group.period.startDate);
    }

    private BooleanExpression studySubjectContain(String subject) {
        return subject != null && !subject.isEmpty() ? QGroup.group.study.subject.contains(subject) : null;
    }

    private BooleanExpression categoryEqual(Category category) {
        return category != null && category.isNotEmpty() ? QGroup.group.study.category.eq(category) : null;
    }

    private BooleanExpression readTypeCond(String readType) {
        if (ConditionUtils.isNotNullAndBlank(readType)) {
            if ("default".equals(readType)) {
                return QGroup.group.groupStatus.in(GroupStatus.GATHERING, GroupStatus.GATHER_COMPLETE, GroupStatus.START);
            }

            if ("gathering".equals(readType)) {
                return QGroup.group.groupStatus.in(GroupStatus.GATHERING);
            }

            if ("all".equals(readType)) {
                return null;
            }
        }
        // 클라이언트가 위 조건과 다른 표현을 사용할 경우 아래 조건이 적용된다.
        return QGroup.group.groupStatus.in(GroupStatus.GATHERING, GroupStatus.GATHER_COMPLETE, GroupStatus.START);
    }

    @Override
    public List<GroupAllQueryResponse> readOwnWithFetch(Long groupMemberId) {
        return queryFactory
                .select(new QGroupAllQueryResponse(
                        QGroup.group.id.as("groupId"),
                        QGroup.group.capacity,
                        QGroup.group.groupStatus,
                        QGroup.group.host,
                        QGroup.group.study,
                        QGroup.group.time,
                        QGroup.group.period
                ))
                .from(QGroup.group)
                .leftJoin(QGroup.group.groupMembers, QGroupMember.groupMember)
                .where(
                        QGroupMember.groupMember.groupMemberId.eq(groupMemberId),
                        QGroupMember.groupMember.isLeft.eq(false)
                )
                .orderBy(readOwnOrderSpec())
                .fetch();
    }

    private OrderSpecifier readOwnOrderSpec() {
        Order direction = Order.DESC;
        return new OrderSpecifier(direction, QGroupMember.groupMember.lastVisitedTime);
    }
}
