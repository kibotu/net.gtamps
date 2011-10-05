package net.gtamps.android.core.utils.cache;

import org.jetbrains.annotations.NotNull;

/**
 * Recycling for objects, used by the {@link ObjectCache}.
 * @see ObjectCache
 */
public interface ObjectFactory<T> {

	/**
	 * Creates a new object and applies the data to it.
	 *
	 * @return The new object
	 */
	@NotNull
	T createNew();
}
