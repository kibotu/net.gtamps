package net.gtamps.shared.configuration;

abstract class AbstractConfigElement implements Configuration {

	private static final long serialVersionUID = -1240349053596166316L;
	
	protected final Class<?> type;
	protected final ConfigSource source;
	
	AbstractConfigElement(Class<?> type, ConfigSource source) {
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
	
}
