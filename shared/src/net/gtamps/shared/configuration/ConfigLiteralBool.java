package net.gtamps.shared.configuration;

public final class ConfigLiteralBool extends AbstractConfigElement {

	private static final long serialVersionUID = -60591522857097794L;

	private final boolean value;

	ConfigLiteralBool(final boolean b, final ConfigSource source) {
		super(Boolean.class, source);
		this.value = b;
	}

	@Override
	public int elementCount() {
		return 0;
	}

	@Override
	public String getString() {
		return Boolean.toString(this.value);
	}

	@Override
	public Boolean getBoolean() {
		return this.value;
	}

	@Override
	public boolean validates() {
		return true;
	}

}
