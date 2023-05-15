package com.jxx.xuni.group.query;

import com.jxx.xuni.studyproduct.domain.Category;
import lombok.Getter;

@Getter
public class GroupSearchCondition {
    private Category category;
    private String readType;

    public GroupSearchCondition(Category category, String readType) {
        this.category = category;
        this.readType = readType;
    }
}
