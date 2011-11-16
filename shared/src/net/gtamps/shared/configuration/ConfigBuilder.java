package net.gtamps.shared.configuration;



public abstract class ConfigBuilder {

	protected final ConfigSource source;
	protected ConfigBuilder parent;

	public static ConfigMapBuilder buildConfig(final ConfigSource source) {
		return new ConfigMapBuilder(source);
	}

	protected ConfigBuilder(final ConfigSource source) {
		this(source, null);
	}

	protected ConfigBuilder(final ConfigSource source, final ConfigBuilder parent) {
		this.source = source;
		this.parent = parent;
	}

	public ConfigBuilder addValue(final int value) {
		addBuilder(new SingletonConfigBuilder(this.source, value, this));
		return this;
	}

	public ConfigBuilder addValue(final float value) {
		addBuilder(new SingletonConfigBuilder(this.source, value, this));
		return this;
	}

	public ConfigBuilder addValue(final boolean value) {
		addBuilder(new SingletonConfigBuilder(this.source, value, this));
		return this;
	}

	public ConfigBuilder addValue(final String value) {
		//TODO null
		addBuilder(new SingletonConfigBuilder(this.source, value, this));
		return this;
	}

	public ConfigBuilder addMap() {
		final ConfigBuilder mapBuilder = new ConfigMapBuilder(this.source, this); 
		addBuilder(mapBuilder);
		return mapBuilder;
	}

	/**
	 * When building a configuration that has sub-elements, select one of the sub-builder by name or index.
	 * If the builder does not exists yet, an empty, single element one will be created, together with the
	 * given key/index. If an invalid index is given, the closest valid index will be used instead. 
	 *  
	 * @param which		for maps: the sub-element's key; for lists: its index
	 * @return this configurationBuilder
	 */
	public ConfigBuilder select(final String key) {
		final ConfigKey ckey = new ConfigKey(key);
		excludeDeepKeys(ckey);
		return select(ckey);
	}

	public ConfigBuilder back() {
		return (this.parent == null) ? this : this.parent;
	}

	public final Configuration getConfig() {
		return this.getBuild();
	}

	@Override
	public String toString() {
		return "ConfigurationBuilder: abstract. A subtype has not overridden toString()";
	}

	//	protected ConfigBuilder addBuilder(final ConfigBuilder cb, final ConfigKey context) {
	//		final ConfigListBuilder listb = new ConfigListBuilder(this.source, this.parent);
	//		this.parent = listb;
	//		listb.addBuilder(this);
	//		listb.addBuilder(cb);
	//		this.parent.update(context, listb);
	//		return back();	// return new list context
	//	}
	public abstract Class<?> getType();

	protected abstract ConfigBuilder addBuilder(final ConfigBuilder cb) throws UnsupportedOperationException;
	protected abstract ConfigBuilder select(ConfigKey ckey);
	//	protected abstract ConfigBuilder update(ConfigKey ckey, ConfigBuilder newBuilder);
	protected abstract Configuration getBuild();

	private void excludeDeepKeys(final ConfigKey ckey) {
		if ("".equals(ckey.tail)) {
			throw new IllegalArgumentException("key level exceeds 1. deep key selection not implemented (yet): " + ckey);
		}
	}

}