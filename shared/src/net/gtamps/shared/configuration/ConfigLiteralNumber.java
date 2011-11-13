package net.gtamps.shared.configuration;


public final class ConfigLiteralNumber extends AbstractConfigElement {

	private static final long serialVersionUID = 1162946319680822921L;
	
	private final Number n;
	
	ConfigLiteralNumber(int i, ConfigSource source) {
		super(Integer.class, source);
		this.n = i;
	}
	
	ConfigLiteralNumber(float f, ConfigSource source) {
		super(Float.class, source);
		this.n = f;
	}

	@Override
	public int elementCount() {
		return 0;
	}

	@Override
	public Configuration get(String key) {
		return null;
	}

	@Override
	public Configuration get(int index) {
		return null;
	}

	@Override
	public String getString() {
		return n.toString();
	}

	@Override
	public Integer getInt() {
		return n.intValue();
	}

	@Override
	public Float getFloat() {
		return n.floatValue();
	}

	@Override
	public Boolean getBoolean() {
		return null;
	}


}
