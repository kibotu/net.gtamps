package net.gtamps.shared.configuration;

import net.gtamps.shared.Utils.Logger;



public abstract class ConfigurationBuilder {

	protected final ConfigSource source;
	protected boolean fixed = false;

	public static ConfigMapBuilder buildConfig(final ConfigSource source) {
		return new ConfigMapBuilder(source);
	}

	protected final static void warnIneffectiveMethod() {
		final String msg = new StringBuffer()
		.append(getCallerInfo(1, true))
		.append(" called ")
		.append(getCallerInfo(0, false))
		.append(", but method has no effect for this builder")
		.toString();
		System.err.println(msg);
		Logger.W(ConfigurationBuilder.class.getSimpleName(), msg);
	}

	private final static String getCallerInfo(final int backtrack, final boolean inclLineNo) {
		final StackTraceElement caller = Thread.currentThread().getStackTrace()[3 + backtrack];
		final String fullName = caller.getClassName();
		final String simpleName = fullName.substring(fullName.lastIndexOf('.') + 1, fullName.length());
		final StringBuffer infobuf = new StringBuffer(simpleName).append('.').append(caller.getMethodName());
		if (inclLineNo) {
			infobuf.append(" (line ").append(caller.getLineNumber()).append(")");
		}
		return infobuf.toString();
	}

	protected ConfigurationBuilder(final ConfigSource source) {
		this.source = source;
	}

	public ConfigurationBuilder addValue(final int value) {
		//		this.add(new ConfigLiteralNumber(value, this.source));
		addConfigurationBuilder(new SingletonConfigBuilder(this.source, value));
		return this;
	}

	public ConfigurationBuilder addValue(final float value) {
		//		this.add(new ConfigLiteralNumber(value, this.source));
		addConfigurationBuilder(new SingletonConfigBuilder(this.source, value));
		return this;
	}

	public ConfigurationBuilder addValue(final boolean value) {
		//		this.add(new ConfigLiteralBool(value, this.source));
		addConfigurationBuilder(new SingletonConfigBuilder(this.source, value));
		return this;
	}

	public ConfigurationBuilder addValue(final String value) {
		//		this.add(new ConfigLiteralString(value, this.source));
		//TODO null
		addConfigurationBuilder(new SingletonConfigBuilder(this.source, value));
		return this;
	}

	public ConfigurationBuilder addSubConfiguration() {
		throw new UnsupportedOperationException("a configuration cannot be added at this point");
	}

	//	/**
	//	 * Adds a sub-configuration to the configuration being built; for multi-element builders
	//	 * (maps, lists), use the sub-element previously {@link #select(String) selected}.
	//	 * 
	//	 * If this builder is single-element, and an element is already set, this builder will be wrapped
	//	 * in a newly created list builder, and the list builder will be returned instead of this.
	//	 * 
	//	 * @return	this configurationBuilder or a new multi-element builder that wraps this one
	//	 */
	//	@Deprecated
	//	private final ConfigurationBuilder add(final Configuration value) {
	//		checkNotFixed();
	//		return addConfiguration(value);
	//	}

	public final ConfigurationBuilder fix() {
		this.fixed = true;
		return this.fixBuild();
	}

	public final Configuration getConfiguration() {
		checkFixed();
		return this.getBuild();
	}

	/**
	 * When building a configuration that has sub-elements, select one of the sub-builder by name or index.
	 * If the builder does not exists yet, an empty, single element one will be created, together with the
	 * given key/index. If an invalid index is given, the closest valid index will be used instead. 
	 *  
	 * @param which		for maps: the sub-element's key; for lists: its index
	 * @return this configurationBuilder
	 */
	public ConfigurationBuilder select(final String which) {
		throw new UnsupportedOperationException(getUnsupportedOperationMsg());
	}

	/**
	 * @return	the configurationBuilder previously {@link #select(String) selected}
	 */
	public ConfigurationBuilder get() {
		throw new UnsupportedOperationException(getUnsupportedOperationMsg());
	}

	protected void updateSelected(final ConfigurationBuilder cb) {
		throw new UnsupportedOperationException(getUnsupportedOperationMsg());
	}


	@Override
	public String toString() {
		return "ConfigurationBuilder: abstract. A subtype has not overridden toString()";
	}


	protected void checkFixed() throws IllegalStateException {
		if (!this.fixed) {
			throw new IllegalStateException("value has not been fixed and cannot be returned. fix() builder first.");
		}
	}

	protected void checkNotFixed() throws IllegalStateException {
		if (this.fixed) {
			throw new IllegalStateException("value has already been fixed and is not editable anymore (single values are always fixed)");
		}
	}

	protected ConfigurationBuilder addConfigurationBuilder(ConfigurationBuilder cb) {
		if (cb == null) {
			throw new IllegalArgumentException("'cb' must not be 'null'");
		}
		checkNotFixed();
		final ConfigurationBuilder existing = get();
		if (existing != null) {
			final ConfigurationBuilder listb = new ConfigListBuilder(existing);
			listb.addConfigurationBuilder(cb);
			cb = listb;
		}
		updateSelected(cb);
		return this;
	}

	protected abstract ConfigurationBuilder fixBuild();
	protected abstract ConfigurationBuilder unfix();
	protected abstract Configuration getBuild();
	protected abstract Class<?> getType();

	private String getUnsupportedOperationMsg() {
		return new StringBuilder(SingletonConfigBuilder.class.getSimpleName())
		.append(" does not support multiple elements")
		.toString();
	}


}