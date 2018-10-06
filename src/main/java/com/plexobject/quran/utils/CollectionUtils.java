package com.plexobject.quran.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> List<T>[] sublist(Collection<T> c, int batchSize) {
        int maxBatches = c.size() % batchSize == 0 ? c.size() / batchSize : (c
                .size() / batchSize) + 1;
        List<T>[] groups = new List[maxBatches];

        List<T> list = c instanceof List ? (List<T>) c : new ArrayList<T>(c);

        for (int i = 0; i < maxBatches; i++) {
            groups[i] = list.subList(i * batchSize, Math.min(c.size(), (i + 1)
                    * batchSize));
        }
        return groups;
    }
}
