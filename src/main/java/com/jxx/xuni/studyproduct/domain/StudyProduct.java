package com.jxx.xuni.studyproduct.domain;

import com.jxx.xuni.common.domain.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyProduct {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "study_product_id")
    private String id;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @Embedded
    private Topic topic;

    @ElementCollection
    @CollectionTable(name = "study_product_content", joinColumns = @JoinColumn(name = "study_product_id"))
    private List<Content> contents = new ArrayList<>();


    public StudyProduct(String name, Category category, Topic topic) {
        this.name = name;
        this.category = category;
        this.topic = topic;
    }
}