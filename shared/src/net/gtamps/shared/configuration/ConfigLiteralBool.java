package net.gtamps.shared.configuration;

public final class ConfigLiteralBool extends AbstractConfigLiteral {

	private static final long serialVersionUID = -60591522857097794L;

	ConfigLiteralBool(final boolean b, final ConfigSource source) {
		super(b, source);
	}

	@Override
	public Boolean getBoolean() {
		return (Boolean) this.value;
	}

	@Override
	public AbstractConfigLiteral clone() {
		return new ConfigLiteralBool(getBoolean(), source);
	}

}
