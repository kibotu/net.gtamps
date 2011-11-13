package net.gtamps.shared.configuration;

abstract class AbstractConfigElement implements Configuration {

	private static final long serialVersionUID = -1240349053596166316L;

	protected final Class<?> type;
	protected final ConfigSource source;

	AbstractConfigElement(final Class<?> type, final ConfigSource source) {
		if (type == null) {
			throw new IllegalArgumentException("'type' must not be 'null'");
		}
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		this.type = type;
		this.source = source;
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	@Override
	public ConfigSource getSource() {
		return this.source;
	}

	@Override
	public Configuration get(final String key) {
		// TODO warn
		return null;
	}

	@Override
	public Configuration get(final int index) {
		// TODO warn
		return null;
	}

	@Override
	public String getString() {
		// TODO warn
		return null;
	}

	@Override
	public Integer getInt() {
		// TODO warn
		return null;
	}

	@Override
	public Float getFloat() {
		// TODO warn
		return null;
	}

	@Override
	public Boolean getBoolean() {
		// TODO warn
		return null;
	}
}
