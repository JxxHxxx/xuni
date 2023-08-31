package com.xuni.common.query;

import lombok.Getter;

@Getter
public class PageInfo {
    private final long offset;
    private final int size;
    private final int pageNumber;
    private final long totalElements;
    private final int totalPage;
    private final boolean last;

    private PageInfo(long offset, int size, int pageNumber, long totalElements, int totalPage, boolean last) {
        this.offset = offset;
        this.size = size;
        this.pageNumber = pageNumber;
        this.totalElements = totalElements;
        this.totalPage = totalPage;
        this.last = last;
    }

    public static PageInfo of(long offset, int size, int pageNumber, long totalElements, int totalPage, boolean last) {
        return new PageInfo(offset, size, pageNumber, totalElements, totalPage, last);
    }
}
