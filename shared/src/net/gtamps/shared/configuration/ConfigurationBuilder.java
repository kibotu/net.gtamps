package net.gtamps.shared.configuration;



public abstract class ConfigurationBuilder {

	protected final ConfigSource source;

	public static ConfigMapBuilder buildConfig(final ConfigSource source) {
		return new ConfigMapBuilder(source, true);
	}

	protected ConfigurationBuilder(final ConfigSource source) {
		this.source = source;
	}

	public ConfigurationBuilder addInt(final int value) {
		this.addConfiguration(new ConfigLiteralNumber(value, this.source));
		return this;
	}

	public ConfigurationBuilder addFloat(final float value) {
		this.addConfiguration(new ConfigLiteralNumber(value, this.source));
		return this;
	}

	public ConfigurationBuilder addBool(final boolean value) {
		this.addConfiguration(new ConfigLiteralBool(value, this.source));
		return this;
	}

	public ConfigurationBuilder addString(final String value) {
		this.addConfiguration(new ConfigLiteralString(value, this.source));
		return this;
	}

	public abstract ConfigurationBuilder addConfiguration(Configuration value);
	public abstract ConfigurationBuilder selectElement(String which);
	public abstract ConfigurationBuilder getSelected();

	protected abstract Configuration getConfigurationElement();


}