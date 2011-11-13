package net.gtamps.shared.configuration;

public final class ConfigLiteralBool extends AbstractConfigElement {
	
	private static final long serialVersionUID = -60591522857097794L;

	private final boolean value;

	ConfigLiteralBool(boolean b, ConfigSource source) {
		super(Boolean.class, source);
		this.value = b;
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
		return null;
	}

	@Override
	public Integer getInt() {
		return null;
	}

	@Override
	public Float getFloat() {
		return null;
	}

	@Override
	public Boolean getBoolean() {
		return value;
	}

}
