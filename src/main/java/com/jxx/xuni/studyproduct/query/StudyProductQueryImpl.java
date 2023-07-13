package com.jxx.xuni.studyproduct.query;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.group.query.dynamic.ConditionUtils;
import com.jxx.xuni.studyproduct.dto.response.QStudyProductQueryResponse;
import com.jxx.xuni.studyproduct.dto.response.StudyProductQueryResponse;
import com.jxx.xuni.studyproduct.query.dynamic.StudyProductSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jxx.xuni.studyproduct.domain.QStudyProduct.*;

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
                        studyProduct.id.as("study_product_id"),
                        studyProduct.name,
                        studyProduct.category,
                        studyProduct.creator,
                        studyProduct.thumbnail
                ))
                .from(studyProduct)
                .where(
                        categoryEqual(condition.category()),
                        creatorEqual(condition.creator()),
                        nameLike(condition.name())
                )
                .offset(pageable.getOffset())
                .fetch();

        long total = queryFactory
                .selectFrom(studyProduct)
                .where(
                        categoryEqual(condition.category()),
                        creatorEqual(condition.creator()),
                        nameLike(condition.name())
                )
                .fetchCount();

        return new PageImpl<StudyProductQueryResponse>(content, pageable, total);
    }

    private BooleanExpression categoryEqual(Category category) {
        return category != null && category.isNotEmpty() ? studyProduct.category.eq(category) : null;
    }

    private BooleanExpression creatorEqual(String creator) {
        return ConditionUtils.isNotNullAndBlank(creator) ? studyProduct.creator.eq(creator) : null;
    }

    private BooleanExpression nameLike(String name) {
        return ConditionUtils.isNotNullAndBlank(name) ? studyProduct.name.contains(name) : null;

    }
}
