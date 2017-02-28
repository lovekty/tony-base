package me.tony.base.util;

import java.util.List;
import java.util.Objects;

/**
 * Created by tony on 2017/2/28.
 */
public final class Lists {
    private Lists() {
    }

    public static <E> E atMostOne(List<E> list) {
        Asserts.must(l -> l.size() < 2, Objects.requireNonNull(list));
        return list.size() == 0 ? null : list.get(0);
    }
}
