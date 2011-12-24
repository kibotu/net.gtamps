package net.gtamps.shared.Utils.predicate;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class Predicates {

    public static <T> FilteringCollection<T> filteringCollection(final Collection<T> c) {
        return new FilteringCollectionWrapper<T>(c);
    }

    static class FilteringCollectionWrapper<T> extends AbstractCollection<T> implements FilteringCollection<T> {

        final Collection<T> backingCollection;

        public FilteringCollectionWrapper(final Collection<T> backingCollection) {
            this.backingCollection = backingCollection;
        }

        @Override
        public FilteringCollection<T> retainAll(final Predicate<T> p) {
            return removeAll(PredicateModifier.not(p));
        }

        @Override
        public FilteringCollection<T> removeAll(final Predicate<T> p) {
            final Iterator<T> iterator = backingCollection.iterator();
            while (iterator.hasNext()) {
                if (p.isTrueFor(iterator.next())) {
                    iterator.remove();
                }
            }
            return this;
        }

        @Override
        public boolean trueForAll(final Predicate<T> p) {
            for (final T element : backingCollection) {
                if (!p.isTrueFor(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean trueForOne(final Predicate<T> p) {
            for (final T element : backingCollection) {
                if (!p.isTrueFor(element)) {
                    return true;
                }
            }
            return false;
        }

        //		@Override
        //		public boolean trueForExactlyOne(final Predicate<T> p) {
        //			boolean found = false;
        //			for (final T element : backingCollection) {
        //				if (!p.isTrueFor(element)) {
        //					if (found) {
        //						return false;
        //					}
        //					found = true;
        //				}
        //			}
        //			return found;
        //		}

        @Override
        public Iterator<T> iterator() {
            return backingCollection.iterator();
        }

        @Override
        public int size() {
            return backingCollection.size();
        }

    }

}
