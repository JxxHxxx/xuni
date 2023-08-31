package com.xuni.studyproduct.query;

import com.xuni.common.domain.Category;
import com.xuni.common.query.ConditionUtils;
import com.xuni.studyproduct.domain.QStudyProduct;
import com.xuni.studyproduct.query.dynamic.StudyProductSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Queydsl을 통해 쿼리문을 작성할 시 아래의 규칙에 따라 작성해주십시오.
 *
 * @See <a href="https://github.com/JxxHxxx/xuni-api-server/wiki/coding-convention#method_rules_querydsl_condition" />
 */
public class StudyProductQueryImpl implements StudyProductQuery {

    private final JPAQueryFactory queryFactory;

    public StudyProductQueryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<StudyProductQueryResponse> searchStudyProduct(StudyProductSearchCondition condition, Pageable pageable) {
        List<StudyProductQueryResponse> content = queryFactory
                .select(new QStudyProductQueryResponse(
                        QStudyProduct.studyProduct.id.as("study_product_id"),
                        QStudyProduct.studyProduct.name,
                        QStudyProduct.studyProduct.category,
                        QStudyProduct.studyProduct.creator,
                        QStudyProduct.studyProduct.thumbnail
                ))
                .from(QStudyProduct.studyProduct)
                .where(
                        categoryEqual(condition.category()),
                        creatorEqual(condition.creator()),
                        nameLike(condition.name())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(QStudyProduct.studyProduct)
                .where(
                        categoryEqual(condition.category()),
                        creatorEqual(condition.creator()),
                        nameLike(condition.name())
                )
                .fetchCount();

        return new PageImpl<StudyProductQueryResponse>(content, pageable, total);
    }

    private BooleanExpression categoryEqual(Category category) {
        return category != null && category.isNotEmpty() ? QStudyProduct.studyProduct.category.eq(category) : null;
    }

    private BooleanExpression creatorEqual(String creator) {
        return ConditionUtils.isNotNullAndBlank(creator) ? QStudyProduct.studyProduct.creator.eq(creator) : null;
    }

    /** SELECT ~ WHERE COLUMN LIKE %name% */
    private BooleanExpression nameLike(String name) {
        return ConditionUtils.isNotNullAndBlank(name) ? QStudyProduct.studyProduct.name.contains(name) : null;

    }
}
