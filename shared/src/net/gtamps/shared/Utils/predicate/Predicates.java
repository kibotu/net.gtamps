package net.gtamps.shared.Utils.predicate;

import java.util.Collection;


public class Predicates {

	@SuppressWarnings("unchecked")
	public static final <T> Predicate<T> not(final Predicate<T> p) {
		return PredicateModifier.NOT.applyTo(p);
	}

	@SuppressWarnings("unchecked")
	public static final <T> Predicate<T> and(final Predicate<T>... p) {
		return PredicateModifier.AND.applyTo(p);
	}

	@SuppressWarnings("unchecked")
	public static final <T> Predicate<T> or(final Predicate<T>... p) {
		return PredicateModifier.OR.applyTo(p);
	}

	public static <T> Predicate<T> alwaysTrue() {
		return new Predicate<T>() {
			@Override
			public boolean appliesTo(final T x) {
				return true;
			}
		};
	}

	public static <T> Predicate<T> alwaysFalse() {
		return new Predicate<T>() {
			@Override
			public boolean appliesTo(final T x) {
				return false;
			}
		};
	}

	public static <T> FilteringCollection<T> filteringCollection(final Collection<T> c) {
		return new FilteringCollectionWrapper<T>(c);
	}


}
