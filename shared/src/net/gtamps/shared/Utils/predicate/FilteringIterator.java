package net.gtamps.shared.Utils.predicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.gtamps.shared.Utils.validate.Validate;

/**
 * an {@link Iterator} that implements a positive filter by skipping over
 * elements which do not satisfy a given {@link Predicate}
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 * @param <E>
 */
public class FilteringIterator<E> implements Iterator<E> {

	private final Predicate<E> filter;
	private final Iterator<E> backingIterator;
	private E next;

	/**
	 * creates a new filtering iterator from <tt>backingIterator</tt>
	 * with the given filter
	 * 
	 * @param filter	the {@link Predicate} that will cause this iterator to skip
	 * 					elements that evaluate to {@code false}; not {@code null}
	 * @param backingIterator	the iterator which will provide the unfiltered elements;
	 * 							not {@code null}
	 */
	public FilteringIterator(final Predicate<E> filter, final Iterator<E> backingIterator) {
		Validate.notNull(filter);
		Validate.notNull(backingIterator);
		this.filter = filter;
		this.backingIterator = backingIterator;
		this.next = findNext();
	}

	private E findNext() {
		E next = null;
		while (next == null && backingIterator.hasNext()) {
			next = backingIterator.next();
			if (!filter.appliesTo(next)) {
				next = null;
			}
		}
		return next;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public E next() {
		if (next == null) {
			throw new NoSuchElementException();
		}
		final E tmp = next;
		next = findNext();
		return tmp;
	}

	@Override
	public void remove() {
		backingIterator.remove();
	}

}
