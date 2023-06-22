package com.example.recipient.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

    public static <T> List<List<T>> splitList(List<T> list, int parts) {
        int size = list.size();
        int partitionSize = (int) Math.ceil((double) size / parts);
        List<List<T>> subLists = new ArrayList<>();

        for (int i = 0; i < size; i += partitionSize) {
            int end = Math.min(i + partitionSize, size);
            subLists.add(list.subList(i, end));
        }

        return subLists;
    }
}
