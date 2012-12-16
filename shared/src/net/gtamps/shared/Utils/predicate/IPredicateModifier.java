package net.gtamps.shared.Utils.predicate;

/**
 * something that creates predicates from existing predicates, effectively
 * creating new behavior
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 * @param <T>
 * @see Predicate
 */
public interface IPredicateModifier<T> {

	/**
	 * apply this predicate modifier to a number of predicates, creating
	 * a new predicate in the process
	 * 
	 * @param subjects	the predicates to be uses as input; not {@code null}
	 * @return	a new predicate based on the input predicates
	 * 
	 * @see Predicate
	 */
	public abstract Predicate<T> applyTo(final Predicate<T>... subjects)
			throws IllegalArgumentException;

}