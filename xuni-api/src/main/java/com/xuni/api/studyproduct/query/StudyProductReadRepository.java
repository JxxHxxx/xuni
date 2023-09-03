package com.xuni.api.studyproduct.query;

import com.xuni.core.common.domain.Category;
import com.xuni.core.studyproduct.domain.StudyProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyProductReadRepository extends JpaRepository<StudyProduct, String>, StudyProductQuery {

    List<StudyProduct> findStudyProductByCategory(Category category);

    @Query(value = "select sp from StudyProduct sp " +
            "join fetch sp.contents spd where sp.id =:studyProductId")
    Optional<StudyProduct> readWithContentFetch(@Param("studyProductId") String studyProductId);

    Page<StudyProduct> readBy(Pageable pageable);
}