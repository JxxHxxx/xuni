package com.xuni.core.studyproduct.domain;

import com.xuni.core.common.domain.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO : 스터디 상품 등록 방식 변경 1. 관리자의 상품 등록 -> 마스터 채널로 슬랙 메시지 -> 마스터의 검수 -> 관리자 상품 등록

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

    public void putContent(Long chapterNo, String title) {
        this.contents.add(Content.of(chapterNo, title));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyProduct that = (StudyProduct) o;
        return id.equals(that.id) && name.equals(that.name) && creator.equals(that.creator) && thumbnail.equals(that.thumbnail) && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creator, thumbnail, category);
    }
}