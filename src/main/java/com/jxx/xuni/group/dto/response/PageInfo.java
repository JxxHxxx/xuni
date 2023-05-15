package com.jxx.xuni.group.dto.response;

import lombok.Getter;

@Getter
public class PageInfo {
    private long offset;
    private int size;
    private int pageNumber;
    private long totalElements;
    private int totalPage;
    private boolean last;

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
