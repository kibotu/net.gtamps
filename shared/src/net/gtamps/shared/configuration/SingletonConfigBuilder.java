package net.gtamps.shared.configuration;

final class SingletonConfigBuilder extends ConfigurationBuilder {

	private Configuration element = null;

	protected SingletonConfigBuilder(final ConfigSource source) {
		super(source);
	}

	@Override
	public ConfigurationBuilder addConfiguration(final Configuration value) {
		ConfigurationBuilder possiblyNewb = this;
		if (this.element == null) {
			this.element = value;
		} else { 
			final ConfigListBuilder list = new ConfigListBuilder(this.source);
			list.addConfiguration(this.getConfigurationElement());
			list.addConfiguration(value);
			possiblyNewb = list;
		}
		return possiblyNewb;
	}

	@Override
	public ConfigurationBuilder selectElement(final String which) {
		//TODO warn
		return this;
	}

	@Override
	public ConfigurationBuilder getSelected() {
		//TODO warn
		return this;
	}

	@Override
	protected Configuration getConfigurationElement() {
		return this.element;
	}

}