package net.gtamps.shared.Utils.predicate;

import java.util.Collection;


/**
 * static utility methods for dealing with {@link Predicate Predicates}
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
public class Predicates {

	/**
	 * negates a {@link Predicate}: returns {@code false} if the original
	 * predicate would return {@code true} and vice versa
	 * 
	 * @param p	the predicate to be negated
	 * @return	a predicate with the opposite behavior of <tt>p</tt>
	 * @throws IllegalArgumentException	if <tt>p</tt> is {@code null}
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Predicate<T> not(final Predicate<T> p) {
		return PredicateModifiers.NOT.applyTo(p);
	}

	/**
	 * merges predicates into a new predicate that returns {@code true} for
	 * a given argument if all of the merged predicates are {@code true}
	 * for this argument
	 * 
	 * @param predicates	the predicates to be joined
	 * @return	the conjunction of the <tt>predicates</tt>
	 * @throws IllegalArgumentException	if no predicates are given
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Predicate<T> and(final Predicate<T>... predicates) {
		return PredicateModifiers.AND.applyTo(predicates);
	}

	/**
	 * merges predicates into a new predicate that returns {@code true} for
	 * a given argument if at least one of the merged predicates is {@code true}
	 * for this argument
	 * 
	 * @param predicates	the predicates to be joined
	 * @return	the disjunction of the <tt>predicates</tt>
	 * @throws IllegalArgumentException	if no predicates are given
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Predicate<T> or(final Predicate<T>... p) {
		return PredicateModifiers.OR.applyTo(p);
	}

	/**
	 * returns a predicate that is always {@code true}, accepting every argument
	 * 
	 * @return	the tautology
	 */
	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> alwaysTrue() {
		return (Predicate<T>) TRUE;
	}

	/**
	 * returns a predicate that is always {@code false}, rejecting every argument
	 * 
	 * @return	the contradiction
	 */
	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> alwaysFalse() {
		return (Predicate<T>) FALSE;
	}

	/**
	 * Gives a collection filtering capabilities. Changes to the original collection
	 * will be reflected in the filtering version.
	 * 
	 * @param c	not {@code null}
	 * @return	a {@link FilteringCollection} backed by <tt>c</tt>
	 */
	public static <T> FilteringCollectionWrapper<T> filteringCollection(final Collection<T> c) {
		return new FilteringCollectionWrapper<T>(c);
	}

	private static final Predicate<?> TRUE = new Predicate<Object>() {
		@Override
		public boolean appliesTo(final Object x) {
			return true;
		}
		@Override
		public String toString() {
			return "alwaysTrue()";
		}
	};

	private static final Predicate<?> FALSE = new Predicate<Object>() {
		@Override
		public boolean appliesTo(final Object x) {
			return false;
		}
		@Override
		public String toString() {
			return "alwaysFalse()";
		}
	};
}
