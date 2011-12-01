package net.gtamps.shared.configuration;

final class ConfigLiteralBuilder extends ConfigBuilder {

	private final Configuration element;

	ConfigLiteralBuilder(final AbstractConfigSource source, final String string, final ConfigBuilder parent) {
		super(source, parent);
		this.element = (string == null) ? null : new ConfigLiteralString(string, source);
	}

	ConfigLiteralBuilder(final AbstractConfigSource source, final int i, final ConfigBuilder parent) {
		super(source, parent);
		this.element = new ConfigLiteralNumber(i, source);
	}

	ConfigLiteralBuilder(final AbstractConfigSource source, final float f, final ConfigBuilder parent) {
		super(source, parent);
		this.element = new ConfigLiteralNumber(f, source);
	}

	ConfigLiteralBuilder(final AbstractConfigSource source, final boolean b, final ConfigBuilder parent) {
		super(source, parent);
		this.element = new ConfigLiteralBool(b, source);
	}

	@Override
	public String toString() {
		return new StringBuilder("Literal (")
		.append(this.element)
		.append(")")
		.toString();
	}

	@Override
	protected ConfigBuilder select(final ConfigKey ckey) {
		throw new UnsupportedOperationException("a single value cannot select from multiple elements");
	}

	@Override
	protected Configuration getBuild() {
		return this.element;
	}

	@Override
	public Class<?> getType() {
		return (this.element == null) ? Object.class : this.element.getType();
	}

	@Override
	protected ConfigBuilder addBuilder(final ConfigBuilder cb)
	throws UnsupportedOperationException {
		final StringBuilder msgBuilder = new StringBuilder("this is a single element. ")
		.append("cannot add additional element ")
		.append(cb.getType().getSimpleName())
		.append(" ")
		.append(cb.toString());
		throw new UnsupportedOperationException(msgBuilder.toString());
	}

	@Override
	public ConfigBuilder addConfig (final Configuration config)
	throws UnsupportedOperationException {
		final StringBuilder msgBuilder = new StringBuilder("this is a single element. ")
		.append("cannot add additional configuration.");
		throw new UnsupportedOperationException(msgBuilder.toString());
	}

	@Override
	public int getCount() {
		return 0;
	}


}