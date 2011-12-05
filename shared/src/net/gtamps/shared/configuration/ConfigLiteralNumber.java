package net.gtamps.shared.configuration;


public final class ConfigLiteralNumber extends AbstractConfigLiteral<Number> {

	private static final long serialVersionUID = 1162946319680822921L;

	private final Class<? extends Number> type;

	ConfigLiteralNumber(final int i, final AbstractConfigSource source) {
		super(i, source);
		this.type = Integer.class;
	}

	ConfigLiteralNumber(final float f, final AbstractConfigSource source) {
		super(f, source);
		this.type = Float.class;
	}

	@Override
	public Class<? extends Number> getType() {
		return this.type;
	}

	@Override
	public Integer getInt() {
		try {
			return (Integer) value;
		} catch (final ClassCastException e) {
			throw new IllegalArgumentException("no int value", e);
		}
	}

	@Override
	public Float getFloat() {
		try {
			return (Float) value;
		} catch (final ClassCastException e) {
			throw new IllegalArgumentException("no float value", e);
		}

	}

	@Override
	public AbstractConfigLiteral<Number> clone() {
		if (value.getClass() == Integer.class) {
			return new ConfigLiteralNumber(getInt(), source);
		} else if (value.getClass() == Float.class) {
			return new ConfigLiteralNumber(getFloat(), source);
		} else {
			throw new IllegalStateException("unknown Number type in value");
		}
	}

}
