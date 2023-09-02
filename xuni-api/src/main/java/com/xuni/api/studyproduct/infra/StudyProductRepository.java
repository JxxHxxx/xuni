package com.xuni.api.studyproduct.infra;

import com.xuni.studyproduct.domain.StudyProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProductRepository extends JpaRepository<StudyProduct, String> {
}