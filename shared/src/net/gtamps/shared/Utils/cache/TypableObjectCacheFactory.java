package net.gtamps.shared.Utils.cache;


/**
 * Creates an {@link IObjectCache} that will store a given type of object
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
public interface TypableObjectCacheFactory {

	/**
	 * Creates an {@link IObjectCache} that will store a given <tt>type</tt> of object
	 * 
	 * @param type
	 * @return	a cache instance that will handle objects of type <tt>type</tt>
	 */
	public <T> IObjectCache<T> createObjectCache(Class<T> type);

}
