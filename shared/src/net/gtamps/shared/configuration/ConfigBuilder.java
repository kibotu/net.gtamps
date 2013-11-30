package net.gtamps.shared.configuration;


public abstract class ConfigBuilder {

	protected final AbstractConfigSource source;
	protected ConfigBuilder parent;

	public static ConfigBuilder buildConfig(final AbstractConfigSource source) {
		return new ConfigMapBuilder(source);
	}

	public static Configuration getEmptyConfiguration() {
		return ConfigBuilder.buildConfig(ConfigSource.EMPTY_SOURCE).getConfig();
	}

	protected ConfigBuilder(final AbstractConfigSource source) {
		this(source, null);
	}

	protected ConfigBuilder(final AbstractConfigSource source, final ConfigBuilder parent) {
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		this.source = source;
		this.parent = parent;
	}

	/**
	 * @return this builder
	 */
	public ConfigBuilder addValue(final int value) {
		addBuilder(new ConfigLiteralBuilder(source, value, this));
		return this;
	}

	/**
	 * @return this builder
	 */
	public ConfigBuilder addValue(final float value) {
		addBuilder(new ConfigLiteralBuilder(source, value, this));
		return this;
	}

	/**
	 * @return this builder
	 */
	public ConfigBuilder addValue(final boolean value) {
		addBuilder(new ConfigLiteralBuilder(source, value, this));
		return this;
	}

	/**
	 * try to cast value as one of the other possible types
	 * (see overloaded {@code addValue(...)} methods) and use one of these;
	 * otherwise, add as String
	 *
	 * @return this builder
	 * @see #addValue(int)
	 * @see #addValue(float)
	 * @see #addValue(boolean)
	 */
	public ConfigBuilder addValue(final String value) {
		final ConfigBuilder otherBuilder = tryAsOtherAllowedType(value);
		if (otherBuilder != null) {
			return otherBuilder;
		} else {
			addBuilder(new ConfigLiteralBuilder(source, value, this));
			return this;
		}
	}

	/**
	 * @return map builder
	 */
	public ConfigBuilder addMap() {
		final ConfigBuilder mapBuilder = new ConfigMapBuilder(source, this);
		addBuilder(mapBuilder);
		return mapBuilder;
	}

	/**
	 * When building a configuration that has sub-elements, select one of the sub-builder by name or index.
	 * If the builder does not exists yet, an empty, single element one will be created, together with the
	 * given key/index. If an invalid index is given, the closest valid index will be used instead.
	 *
	 * @param key	for maps: the sub-element's key; for lists: its index
	 * @return this configurationBuilder
	 */
	public ConfigBuilder select(final String key) {
		final ConfigKey ckey = new ConfigKey(key);
		excludeDeepKeys(ckey);
		return select(ckey);
	}

	/**
	 * @return parent builder; if {@code parent == null}: this builder
	 */
	public ConfigBuilder back() {
		return (parent == null) ? this : parent;
	}

	/**
	 * @return the configuration as internalized by this builder
	 */
	public final Configuration getConfig() {
		return getBuild();
	}

	/**
	 * @return the number of child builders in this builder
	 */
	public abstract int getCount();

	@Override
	public String toString() {
		return "ConfigurationBuilder: abstract. A subtype has not overridden toString()";
	}


	/**
	 * @return the type of this builder, which equals the {@link Configuration#getType() type}
	 * of the Configuration buing built.
	 */
	public abstract Class<?> getType();

	/**
	 * directly add a configuration to this builder. unless {@code config} is
	 * {@code null}, this should increase its {@link #getCount() count}.
	 *
	 * @param config
	 * @return this builder
	 * @see Configuration
	 */
	public abstract ConfigBuilder addConfig(Configuration config);

	protected abstract ConfigBuilder addBuilder(final ConfigBuilder cb) throws UnsupportedOperationException;

	protected abstract ConfigBuilder select(ConfigKey ckey);

	protected abstract Configuration getBuild();

	private void excludeDeepKeys(final ConfigKey ckey) {
		if ("".equals(ckey.tail)) {
			throw new IllegalArgumentException("key level exceeds 1. deep key selection not implemented (yet): " + ckey);
		}
	}

	/**
	 * @return valid ConfigBuilder if <tt>value</tt> has been added as another type,
	 * or {@code null}
	 */
	private ConfigBuilder tryAsOtherAllowedType(final String value) {
		if (value != null) {
			final Integer i = tryAsInteger(value);
			if (i != null) {
				return addValue(i);
			}
			final Float f = tryAsFloat(value);
			if (f != null) {
				return addValue(f);
			}
			final Boolean b = tryAsBoolean(value);
			if (b != null) {
				return addValue(b);
			}
		}
		return null;
	}

	private Boolean tryAsBoolean(final String value) {
		Boolean b = null;
		if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
			b = Boolean.valueOf(value);
		}
		return b;
	}

	private Float tryAsFloat(final String value) {
		Float f = null;
		try {
			f = Float.parseFloat(value);
		} catch (final NumberFormatException e) {
		}
		return f;
	}

	private Integer tryAsInteger(final String value) {
		Integer i = null;
		try {
			i = Integer.parseInt(value, 10);
		} catch (final NumberFormatException e) {
		}
		return i;
	}

}