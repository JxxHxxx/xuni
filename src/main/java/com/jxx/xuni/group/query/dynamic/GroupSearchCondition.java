package com.jxx.xuni.group.query.dynamic;

import com.jxx.xuni.common.domain.Category;

public class GroupSearchCondition {
    private Category category;
    private String readType;
    private Boolean isAsc;
    private String sortProperty;

    public GroupSearchCondition(Category category, String readType, Boolean isAsc, String sortProperty) {
        this.category = category;
        this.readType = readType;
        this.isAsc = isAsc;
        this.sortProperty = sortProperty;
    }

    public void nullHandle() {
        if (this.isAsc == null) this.isAsc = false;
    }

    public Category getCategory() {
        return category;
    }

    public String getReadType() {
        return readType;
    }

    public String getSortProperty() {
        return sortProperty;
    }

    public Boolean isAsc() {
        return isAsc;
    }
}
