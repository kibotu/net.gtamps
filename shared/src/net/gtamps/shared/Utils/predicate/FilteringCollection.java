package net.gtamps.shared.Utils.predicate;

import java.util.Collection;

public interface FilteringCollection<T> extends Collection<T> {
    public FilteringCollection<T> retainAll(Predicate<T> p);

    public FilteringCollection<T> removeAll(Predicate<T> p);

    public boolean trueForAll(Predicate<T> p);

    public boolean trueForOne(Predicate<T> p);

}
