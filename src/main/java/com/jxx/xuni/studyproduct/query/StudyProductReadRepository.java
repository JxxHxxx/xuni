package com.jxx.xuni.studyproduct.query;

import com.jxx.xuni.studyproduct.domain.Category;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyProductReadRepository extends JpaRepository<StudyProduct, String> {

    List<StudyProduct> findStudyProductByCategory(Category category);
}