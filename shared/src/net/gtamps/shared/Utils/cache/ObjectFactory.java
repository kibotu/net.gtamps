package net.gtamps.shared.Utils.cache;

import net.gtamps.shared.Utils.cache.annotations.ReturnsCachedValue;
import org.jetbrains.annotations.NotNull;

/**
 * Recycling for objects, used by the {@link RealObjectCache}.
 * @see RealObjectCache
 */
public interface ObjectFactory<T> {

	/**
	 * Creates a new object and applies the data to it.
	 *
	 * @return The new object
	 */
	@NotNull
	@ReturnsCachedValue
	T createNew();
}
