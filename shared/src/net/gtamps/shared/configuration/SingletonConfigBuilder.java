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
			list.addConfiguration(this.element);
			list.addConfiguration(value);
			possiblyNewb = list;
		}
		return possiblyNewb;
	}

	@Override
	public ConfigurationBuilder select(final String which) {
		if (this.element != null) {
			throw new IllegalStateException("this element is already defined as something different from a Map");
		}

		warnIneffectiveMethod();
		return this;
	}

	@Override
	public ConfigurationBuilder getSelected() {
		warnIneffectiveMethod();
		return this;
	}

	@Override
	public ConfigurationBuilder fixBuild() {
		assert this.validates() : "validation error in element: " + this.element.toString();
		return this;
	}

	@Override
	public Configuration getBuild() {
		return this.element;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Literal (")
		.append(this.fixed ? "fixed) " : "building): ")
		.append(this.element);
		return sb.toString();
	}


	protected boolean validates() {
		if (this.element != null) {
			return this.element.validates();
		}
		return true;
	}

}