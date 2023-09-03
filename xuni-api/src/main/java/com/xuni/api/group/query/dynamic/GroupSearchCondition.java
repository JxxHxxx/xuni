package com.xuni.api.group.query.dynamic;

import com.xuni.core.common.domain.Category;

public class GroupSearchCondition {
    private Category category;
    private String readType;
    private Boolean isAsc;
    private String sortProperty;
    private String subject;

    public GroupSearchCondition(Category category, String readType, Boolean isAsc, String sortProperty, String subject) {
        this.category = category;
        this.readType = readType;
        this.isAsc = isAsc;
        this.sortProperty = sortProperty;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }
}
