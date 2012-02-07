package net.gtamps.shared.Utils.predicate;

/**
 * Represents a function that maps a boolean value to an argument object.
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 * @param <T>
 */
public interface Predicate<T> {
	/**
	 * {@code true} if the argument satisfies the semantics of this predicate
	 * 
	 * @param x	the test object to be evaluated
	 * @return	{@true} or {@code false}
	 */
	boolean appliesTo(T x);
}
