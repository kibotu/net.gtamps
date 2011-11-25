package net.gtamps.shared.configuration;

public final class ConfigLiteralString extends AbstractConfigLiteral {

	private static final long serialVersionUID = 6917569881343326134L;

	ConfigLiteralString(final String string, final AbstractConfigSource source) {
		super(string, source);
	}

	@Override
	public AbstractConfigLiteral clone() {
		return new ConfigLiteralString(getString(), source);
	}

}
