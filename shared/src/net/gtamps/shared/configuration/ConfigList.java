package net.gtamps.shared.configuration;

import java.util.AbstractList;
import java.util.List;

public final class ConfigList extends AbstractList<Configuration> implements Configuration {
	
	private static final long serialVersionUID = -2683152299650610400L;
	
	private final Class<?> type = List.class;
	
	private final Configuration[] entries;
	private int size = 0;
	
	private ConfigSource source;

	ConfigList(ConfigSource source) {
		this(source, 10);
	}
	
	ConfigList(ConfigSource source, int initSize) {
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		if (initSize < 0) {
			throw new IllegalArgumentException("'initSize' must not be >= 0");
		}
		this.source = source;
		this.entries = new Configuration[initSize];
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public int elementCount() {
		return size;
	}

	@Override
	public Configuration get(String key) {
		Configuration element = null;
		try {
			element = get(Integer.valueOf(key));
		} catch (NumberFormatException e){
			// hickey-dee-doo!
		}
		return element;
	}

	@Override
	public String getString() {
		return entries.toString();
	}

	@Override
	public Integer getInt() {
		// TODO Auto-generated method stub
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

	@Override
	public ConfigSource getSource() {
		return source;
	}

	@Override
	public Configuration get(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("'index' must be >= 0");
		}
		if (index >= elementCount()) {
			throw new IndexOutOfBoundsException(String.format(
					"index out of bounds (%d): %d", size(), index));
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}
	
	boolean validates() throws AssertionError {
		return List.class.equals(type) && source != null && elementCount() > 0;
		//TODO immutability
	}

}
