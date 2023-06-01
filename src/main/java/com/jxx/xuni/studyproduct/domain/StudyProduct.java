package com.jxx.xuni.studyproduct.domain;

import com.jxx.xuni.common.domain.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyProduct {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "study_product_id")
    private String id;
    @Column(name = "study_product_name")
    private String name; // 상품 명
    private String creator;
    private String thumbnail;
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @ElementCollection
    @CollectionTable(name = "study_product_content", joinColumns = @JoinColumn(name = "study_product_id"))
    private List<Content> contents = new ArrayList<>();

    @Builder
    public StudyProduct(String name, String creator, String thumbnail, Category category) {
        this.name = name;
        this.creator = creator;
        this.thumbnail = thumbnail;
        this.category = category;
    }
}