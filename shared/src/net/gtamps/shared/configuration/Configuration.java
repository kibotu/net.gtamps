package net.gtamps.shared.configuration;

import java.io.Serializable;
import java.util.Collection;

public interface Configuration extends Cloneable, Serializable, Iterable<Configuration> {

	/** the character used to delimit different layers of a configuration key, and
	 * thus allows to bridge several layers of hierarchy */
	public static final char KEY_DELIMITER = '.'; 

	/**
	 * @return	one of Map.class, List.class, Integer.class, Float.class, Boolean.class, String.class
	 * 			Map implied a Map<String, Configuration>, List is a List<Configuration> 
	 */
	public Class<?> getType();

	/** @return the number of child-configurations */
	public int getCount();

	/**
	 * @param key	either a name (starts with letter or underscore, case-insensitive),
	 * 				or a numerical string used as index
	 * @return		the configuration fitting <tt>key</tt>, or <code>null</code>
	 */
	public Configuration select(String key);

	/**
	 * @param index	for a list config element, 
	 * @return	the sub-configuration with the given <tt>index</tt>; for a list, this
	 * 			is consistent, for a map, exact behavior is not defined
	 */
	public Configuration select(int index);

	/** @return get a string representation of the stored value; for maps and lists, this is purely informative */
	public String getString();

	/** @return an integer representation of the stored value, or <code>null</code>, if none exists */
	public Integer getInt();

	/** @return a float representation of the stored value, or <code>null</code>, if none exists */	
	public Float getFloat();

	/** @return a boolean representation of the stored value, or <code>null</code>, if none exists 
	 * 			only guaranteed to behave as expected for String inputs of case-insensitive forms of 
	 * 			<code>true</code> and <code>false</code>.	
	 * */
	public Boolean getBoolean();

	/** @return the source of the configuration. only meaningful for single value types, not for maps or lists */
	public ConfigSource getSource();

	/**
	 * @return 	a collection of the valid keys to this configuration. Does not include magic keys that
	 * 			make use of {@linkplain #KEY_DELIMITER} to bridge several layers of mapping.
	 */
	public Collection<String> getKeys() ;

	public Configuration clone();

}
