package net.gtamps.shared.configuration;

public final class ConfigLiteralBool extends AbstractConfigLiteral<Boolean> {

	private static final long serialVersionUID = -60591522857097794L;

	ConfigLiteralBool(final boolean b, final AbstractConfigSource source) {
		super(b, source);
	}

	@Override
	public Boolean getBoolean() {
		return this.value;
	}

	@Override
	public AbstractConfigLiteral<Boolean> clone() {
		return new ConfigLiteralBool(getBoolean(), source);
	}

}
