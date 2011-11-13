package net.gtamps.shared.configuration;

public final class ConfigLiteralString extends AbstractConfigElement {

	private static final long serialVersionUID = 6917569881343326134L;

	private final String value;

	ConfigLiteralString(final String string, final ConfigSource source) {
		super(java.lang.String.class, source);
		this.value = (string == null) ? "" : string;
	}

	@Override
	public int elementCount() {
		return 0;
	}

	@Override
	public String getString() {
		return this.value;
	}

	@Override
	public boolean validates() {
		return this.value != null;
	}

}
