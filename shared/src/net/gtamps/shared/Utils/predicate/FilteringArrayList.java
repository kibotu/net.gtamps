package net.gtamps.shared.Utils.predicate;

import java.util.ArrayList;
import java.util.Arrays;


public class FilteringArrayList<T> extends ArrayList<T> implements FilteringCollection<T> {
    public static <A> FilteringArrayList<A> fromArray(final A[] a) {
        final FilteringArrayList<A> list = new FilteringArrayList<A>();
        list.ensureCapacity(a.length);
        list.addAll(Arrays.asList(a));
        return list;
    }

    @Override
    public FilteringArrayList<T> retainAll(final Predicate<T> p) {
        removeAll(PredicateModifier.not(p));
        return this;
    }

    @Override
    public FilteringArrayList<T> removeAll(final Predicate<T> p) {
        int i = 0;
        while (i < size()) {
            if (p.isTrueFor(get(i))) {
                remove(i);
            } else {
                i++;
            }
        }
        return this;
    }

    @Override
    public boolean trueForAll(final Predicate<T> p) {
        return !trueForOne(PredicateModifier.not(p));
    }

    @Override
    public boolean trueForOne(final Predicate<T> p) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (p.isTrueFor(get(i))) {
                return true;
            }
        }
        return false;
    }

}
