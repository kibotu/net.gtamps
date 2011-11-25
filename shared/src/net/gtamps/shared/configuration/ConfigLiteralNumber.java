package net.gtamps.shared.configuration;


public final class ConfigLiteralNumber extends AbstractConfigLiteral {

	private static final long serialVersionUID = 1162946319680822921L;

	ConfigLiteralNumber(final int i, final AbstractConfigSource source) {
		super(i, source);
	}

	ConfigLiteralNumber(final float f, final AbstractConfigSource source) {
		super(f, source);
	}

	@Override
	public Integer getInt() {
		return (Integer) value;
	}

	@Override
	public Float getFloat() {
		return (Float) value;
	}

	@Override
	public AbstractConfigLiteral clone() {
		if (value.getClass() == Integer.class) {
			return new ConfigLiteralNumber(getInt(), source);
		} else if (value.getClass() == Float.class) {
			return new ConfigLiteralNumber(getFloat(), source);
		} else {
			throw new IllegalStateException("unknown Number type in value");
		}
	}

}
