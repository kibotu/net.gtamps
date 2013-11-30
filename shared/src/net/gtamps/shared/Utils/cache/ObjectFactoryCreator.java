package net.gtamps.shared.Utils.cache;


/**
 * A factory for {@link ObjectFactory ObjectFactories}, which is useful
 * for automatic creation of factory clients, like caches with instantiating 
 * abilities.
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 * @param <T>	unrestricted for now, but may be narrowed by specific implementations
 */
public interface ObjectFactoryCreator<T> {

	/**
	 * Creates a factory for objects of a given {@code type}
	 * 
	 * @param type	the type of objects the created factory will generate
	 * @return 		a factory for objects of a given {@code type}
	 * @throws IllegalArgumentException	when an illegal {@code type} is given   
	 */
	public ObjectFactory<T> createFactory(Class<T> type) throws IllegalArgumentException;

}
