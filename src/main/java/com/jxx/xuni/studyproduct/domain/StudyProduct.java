package com.jxx.xuni.studyproduct.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyProduct {

    @Id
    private String id;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Category category;
    @Embedded
    private Topic topic;

    public StudyProduct(String name, Category category, Topic topic) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.topic = topic;
    }
}