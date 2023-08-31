package com.xuni.common.query;

import org.springframework.stereotype.Service;

@Service
public class PagingModifier {

    private static final int MAX_SIZE = 100;
    private static final int DEFAULT_SIZE = 20;
    private static final int PAGE_MIN = 0;

    public ModifiedPagingForm modify(int page, int size) {
        return new ModifiedPagingForm(modifyPage(page), modifySize(size));
    }

    private int modifySize(int size) {
        if (size < 0) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }

    private int modifyPage(int page) {
        return Math.max(page, PAGE_MIN);
    }
}
