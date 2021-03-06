package net.gtamps.shared.Utils.predicate;

import java.util.Collection;

/**
 * A collection that can be made to ignore some of its elements by setting a
 * filter in the form of a {@link Predicate}. Elements which do not satisfy the
 * predicate (it evaluates to {@code false} for the specific element) are kept
 * in the collection, but are not counted towards its size, and will be skipped
 * by the collection's iterators.
 * <p/>
 * If the collection is mutable, changes will only apply to elements that
 * satisfy the filtering predicate. What passes the filter gets added or removed;
 * what doesn't, doesn't. No surprises.
 * 
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 * @param <T>
 * @see Predicate
 */
public interface FilteringCollection<T> extends Collection<T> {

	/**
	 * returns the filtering predicate currently in use for this filtering collection
	 * @return the filtering predicate currently in use for this filtering collection
	 * 
	 * @see Predicate
	 */
	public Predicate<T> getFilter();

	/**
	 * Returns a filtering collection with the {@link Predicate} set that will operate
	 * as its filter by counting in all elements that satisfy the predicate, and
	 * discounting all elements for which the predicate evaluates to
	 * {@code false}.
	 * <p/>
	 * It is left up to the implementation whether this will be a new collection
	 * or the same one with the filter changed in place. Changing the filter
	 * predicate like that will not affect previously created
	 * iterators and will therefore not change the behavior of ongoing
	 * iterations.
	 * 
	 * @param filter	the predicate that will be
	 * 					{@link Predicate#appliesTo(Object) applied}	to this
	 * 					collection's elements as the filter; not {@code null}
	 * @return a filtering collection with its filtering predicate set to <tt>filter</tt>
	 * @see Predicate
	 */
	public FilteringCollection<T> withFilter(Predicate<T> filter);

	@Override
	public int size();

	@Override
	public FilteringIterator<T> iterator();

	/**
	 * {@inheritDoc}
	 * 
	 * The element gets added to this collection if it satisfies the
	 * {@link #getFilter() filter}.
	 * <p/>
	 * If the filter rejects it, the element is not added at all. Not even in
	 * a hidden way: subsequently changing the collection's filter to one
	 * that accepts the element will not cause the element to suddenly appear;
	 * because it's not there.
	 * 
	 * @param e	the element to be added
	 * @return {@code true} if the element was added
	 * @throws UnsupportedOperationException if this collection is immutable
	 * 
	 */
	@Override
	public boolean add(T e) throws UnsupportedOperationException;

	/**
	 * {@code true} if the collection's {@link #withFilter(Predicate) filter}
	 * predicate is <tt>true</tt> for all elements in this collection, making it
	 *  <strong>{@code true}</strong> if this collection
	 * is {@link java.util.Collection#isEmpty() empty}.
	 * 
	 * @return	{@code true} if the this collection's filter evaluates to true for
	 * 			every collection element, or if this collection is empty
	 * @see #withFilter(Predicate)
	 * @see Predicate
	 */
	public boolean trueForAll();

	/**
	 * {@code true} if there is at least one element in this collection that
	 * satifies the collection's {@link #withFilter(Predicate) filter} predicate,
	 * making it <strong>{@code false}</strong> if this collection is {@link java.util.Collection#isEmpty() empty}.
	 * 
	 * @return	{@code true} if the filter predicate is true for at least one element;
	 * 			{@code false} otherwise, especially if this collectionis empty
	 * @see #withFilter(Predicate)
	 * @see Predicate
	 */
	public boolean trueForOne();


}
