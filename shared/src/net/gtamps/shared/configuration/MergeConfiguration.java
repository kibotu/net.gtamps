package net.gtamps.shared.configuration;

import java.util.Collection;
import java.util.Iterator;

/**
 * A configuration that can have other configurations merged into it.
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
public final class MergeConfiguration implements Configuration {

	private static final long serialVersionUID = -5647708565653789459L;

	MergeStrategy mergeStrategy;
	Configuration baseConfig = null;

	/**
	 * Creates an empty configuration that can be merged with others.
	 * @param mergeStrategy	the {@link MergeStrategy} to use; not {@code null}
	 */
	public MergeConfiguration(final MergeStrategy mergeStrategy) {
		this(mergeStrategy, null);
	}

	/**
	 * @param mergeStrategy	not {@code null}
	 * @param baseConfig	when {@code null}, an empty config will be created
	 */
	public MergeConfiguration(final MergeStrategy mergeStrategy, final Configuration baseConfig) {
		if (mergeStrategy == null) {
			throw new IllegalArgumentException("'mergeStrategy' must not be 'null'");
		}
		this.mergeStrategy = mergeStrategy;
		this.baseConfig = (baseConfig == null) ? 
				new ConfigMap(ConfigSource.EMPTY_SOURCE) : baseConfig;
	}

	/**
	 * Merges another configuration into this one using the 
	 * {@link MergeStrategy} as defines in the
	 * {@link #MergeConfiguration(MergeStrategy, Configuration) constructor}.
	 *  
	 * @param otherConfig	the configuration to merge into this one
	 * @return	this configuration, after the merge
	 */
	public MergeConfiguration merge(final Configuration otherConfig) {
		this.baseConfig = mergeStrategy.merge(baseConfig, otherConfig);
		return this;
	}

	@Override
	public Class<?> getType() {
		return baseConfig.getType();
	}

	@Override
	public int getCount() {
		return baseConfig.getCount();
	}

	@Override
	public Configuration select(final String key) {
		return baseConfig.select(key);
	}

	@Override
	public Configuration select(final int index) {
		return baseConfig.select(index);
	}

	@Override
	public String getString() {
		return baseConfig.getString();
	}

	@Override
	public Integer getInt() {
		return baseConfig.getInt();
	}

	@Override
	public Iterator<Configuration> iterator() {
		return baseConfig.iterator();
	}

	@Override
	public Float getFloat() {
		return baseConfig.getFloat();
	}

	@Override
	public Boolean getBoolean() {
		return baseConfig.getBoolean();
	}

	@Override
	public ConfigSource getSource() {
		return baseConfig.getSource();
	}

	@Override
	public Collection<String> getKeys() {
		return baseConfig.getKeys();
	}

	@Override
	public Object clone() {
		return baseConfig.clone();
	}

	@Override
	public boolean equals(final Object o) {
		return baseConfig.equals(o);
	}

	@Override
	public int hashCode() {
		return baseConfig.hashCode();
	}

}
