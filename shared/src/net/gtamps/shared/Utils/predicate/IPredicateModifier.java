package net.gtamps.shared.Utils.predicate;

public interface IPredicateModifier<T> {

	/**
	 * @param subjects
	 * @return
	 */
	public abstract Predicate<T> applyTo(final Predicate<T>... subjects)
			throws IllegalArgumentException;

}