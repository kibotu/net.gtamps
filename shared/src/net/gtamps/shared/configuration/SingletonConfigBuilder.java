package net.gtamps.shared.configuration;

final class SingletonConfigBuilder extends ConfigurationBuilder {

	private final Configuration element;

	SingletonConfigBuilder(final ConfigSource source, final String string) {
		super(source);
		this.element = (string == null) ? null : new ConfigLiteralString(string, source);
		this.fixed = true;
	}

	SingletonConfigBuilder(final ConfigSource source, final int i) {
		super(source);
		this.element = new ConfigLiteralNumber(i, source);
	}

	SingletonConfigBuilder(final ConfigSource source, final float f) {
		super(source);
		this.element = new ConfigLiteralNumber(f, source);
	}

	SingletonConfigBuilder(final ConfigSource source, final boolean b) {
		super(source);
		this.element = new ConfigLiteralBool(b, source);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Literal (")
		.append(this.fixed ? "fixed) " : "building): ")
		.append(this.element);
		return sb.toString();
	}

	//	@Override
	//	protected ConfigurationBuilder addConfigurationBuilder(final ConfigurationBuilder cb) {
	// 		// override to prevent UnsupportedOperationException and throw a warning or something
	//	}

	@Override
	protected ConfigurationBuilder fixBuild() {
		assert this.validates() : "validation error in element: " + this.element.toString();
		return this;
	}

	@Override
	protected ConfigurationBuilder unfix() {
		this.fixed = true;	// don't unfix
		return this;
	}

	@Override
	protected Configuration getBuild() {
		return this.element;
	}

	@Override
	protected Class<?> getType() {
		return (this.element == null) ? null : this.element.getType();
	}

	protected boolean validates() {
		if (this.element != null) {
			return this.element.validates();
		}
		return true;
	}

}