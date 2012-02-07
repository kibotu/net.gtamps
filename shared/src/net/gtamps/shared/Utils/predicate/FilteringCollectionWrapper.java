package net.gtamps.shared.Utils.predicate;

import java.util.AbstractCollection;
import java.util.Collection;

import net.gtamps.shared.Utils.validate.Validate;

/**
 * A wrapper that adds filtering capabilities to an existing collection;
 * changes to the original collection will be reflected in its filtering version.
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 * @param <T>
 */
class FilteringCollectionWrapper<T> extends AbstractCollection<T> implements FilteringCollection<T> {

	private final Collection<T> backingCollection;
	private final Predicate<T> filter;

	/**
	 * creates a new filtering collection backed by <tt>backingCollection</tt>
	 * with the tautology (always true) as filter.
	 * 
	 * @param backingCollection	not {@code null}
	 */
	public FilteringCollectionWrapper(final Collection<T> backingCollection) {
		this(backingCollection, Predicates.<T>alwaysTrue());
	}

	private FilteringCollectionWrapper(final Collection<T> backingCollection, final Predicate<T> filter) {
		Validate.notNull(backingCollection);
		Validate.notNull(filter);
		this.backingCollection = backingCollection;
		this.filter = filter;
	}

	@Override
	public Predicate<T> getFilter() {
		return filter;
	}

	@Override
	public FilteringCollectionWrapper<T> withFilter(final Predicate<T> filter) {
		Validate.notNull(filter);
		return new FilteringCollectionWrapper<T>(backingCollection, filter);
	}

	@Override
	public FilteringIterator<T> iterator() {
		return new FilteringIterator<T>(filter, backingCollection.iterator());
	}

	@Override
	public int size() {
		int sum = 0;
		for (@SuppressWarnings("unused") final T element: this) {
			sum++;
		}
		return sum;
	}

	@Override
	public boolean add(final T e) {
		if (filter.appliesTo(e)) {
			return backingCollection.add(e);
		}
		return false;
	}

	@Override
	public boolean trueForAll() {
		return this.size() == backingCollection.size();
	}

	@Override
	public boolean trueForOne() {
		return this.size() > 0;
	}

}