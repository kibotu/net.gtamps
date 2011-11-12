package net.gtamps.shared.configuration;

abstract class AbstractConfigElement implements Configuration {

	private static final long serialVersionUID = -1240349053596166316L;
	
	static String normalizeKey(String key) {
		return key.toUpperCase();
	}
	
	static String[] splitKey(String key) {
		int index = key.indexOf(KEY_DELIMITER);
		String[] pair = new String[2];
		pair[0] = key.substring(0, index);			
		pair[1] = key.substring(index+1);			
		return pair;
	}
	
	static String assembleKey(String base, String tail) {
		int size = base.length() + KEY_DELIMITER.length() + tail.length();
		return new StringBuilder(size).append(base).append(KEY_DELIMITER).append(tail).toString();
	}
	
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
