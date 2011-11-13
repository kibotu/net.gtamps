package net.gtamps.shared.configuration;



public abstract class ConfigurationBuilder {

	protected final ConfigSource source;
	protected boolean fixed = false;

	public static ConfigMapBuilder buildConfig(final ConfigSource source) {
		return new ConfigMapBuilder(source, true);
	}

	protected ConfigurationBuilder(final ConfigSource source) {
		this.source = source;
	}

	public ConfigurationBuilder addInt(final int value) {
		this.add(new ConfigLiteralNumber(value, this.source));
		return this;
	}

	public ConfigurationBuilder addFloat(final float value) {
		this.add(new ConfigLiteralNumber(value, this.source));
		return this;
	}

	public ConfigurationBuilder addBool(final boolean value) {
		this.add(new ConfigLiteralBool(value, this.source));
		return this;
	}

	public ConfigurationBuilder addString(final String value) {
		this.add(new ConfigLiteralString(value, this.source));
		return this;
	}

	/**
	 * Adds a sub-configuration to the configuration being built; for multi-element builders
	 * (maps, lists), use the sub-element previously {@link #selectElement(String) selected}.
	 * 
	 * If this builder is single-element, and an element is already set, this builder will be wrapped
	 * in a newly created list builder, and the list builder will be returned instead of this.
	 * 
	 * @return	this configurationBuilder or a new multi-element builder that wraps this one
	 */
	public final ConfigurationBuilder add(final Configuration value) {
		checkNotFixed();
		return addConfiguration(value);
	}

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
	public abstract ConfigurationBuilder selectElement(String which);

	/**
	 * @return	the configurationBuilder previously {@link #selectElement(String) selected}
	 */
	public abstract ConfigurationBuilder getSelected();


	protected void checkFixed() throws IllegalStateException {
		if (!this.fixed) {
			throw new IllegalStateException("build has already been fixed and is not editable anymore");
		}
	}

	protected void checkNotFixed() throws IllegalStateException {
		if (this.fixed) {
			throw new IllegalStateException("build has already been fixed and is not editable anymore");
		}
	}

	protected abstract ConfigurationBuilder addConfiguration(Configuration value);
	protected abstract ConfigurationBuilder fixBuild();
	protected abstract Configuration getBuild();

}