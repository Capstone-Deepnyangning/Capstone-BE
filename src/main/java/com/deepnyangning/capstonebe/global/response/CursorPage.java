package com.deepnyangning.capstonebe.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CursorPage<T> {
    private List<T> content;
    private boolean hasNext;

    public static <T> CursorPage<T> of(List<T> list, int size) {
        boolean hasNext = list.size() > size;
        List<T> content = hasNext ? list.subList(0, size) : list;
        return new CursorPage<>(content, hasNext);
    }
}