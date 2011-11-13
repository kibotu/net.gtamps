package net.gtamps.shared.configuration;


public final class ConfigLiteralNumber extends AbstractConfigElement {

	private static final long serialVersionUID = 1162946319680822921L;

	private final Number n;

	ConfigLiteralNumber(final int i, final ConfigSource source) {
		super(Integer.class, source);
		this.n = i;
	}

	ConfigLiteralNumber(final float f, final ConfigSource source) {
		super(Float.class, source);
		this.n = f;
	}

	@Override
	public int elementCount() {
		return 0;
	}

	@Override
	public String getString() {
		return this.n.toString();
	}

	@Override
	public Integer getInt() {
		return this.n.intValue();
	}

	@Override
	public Float getFloat() {
		return this.n.floatValue();
	}

	@Override
	public boolean validates() {
		return this.n != null;
	}

	@Override
	public String toString() {
		return this.n.toString();
	}

}
