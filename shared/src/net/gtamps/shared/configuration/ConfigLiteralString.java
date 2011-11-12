package net.gtamps.shared.configuration;

public final class ConfigLiteralString extends AbstractConfigElement {

	private static final long serialVersionUID = 6917569881343326134L;
	
	private final String value;

	ConfigLiteralString(String string, ConfigSource source) {
		super(java.lang.String.class, source);
		this.value = (string == null) ? "" : string;
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
		return value;
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
		return null;
	}

}
