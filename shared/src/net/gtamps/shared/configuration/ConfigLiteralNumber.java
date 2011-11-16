package net.gtamps.shared.configuration;


public final class ConfigLiteralNumber extends AbstractConfigLiteral {

	private static final long serialVersionUID = 1162946319680822921L;

	ConfigLiteralNumber(final int i, final ConfigSource source) {
		super(i, source);
	}

	ConfigLiteralNumber(final float f, final ConfigSource source) {
		super(Float.class, source);
	}

	@Override
	public Integer getInt() {
		return (Integer) value;
	}

	@Override
	public Float getFloat() {
		return (Float) value;
	}

}
