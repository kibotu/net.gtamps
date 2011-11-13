package net.gtamps.shared.configuration;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public final class ConfigList extends AbstractList<Configuration> implements Configuration {

	private static final long serialVersionUID = -2683152299650610400L;

	private final Class<?> type = List.class;
	private final List<Configuration> entries = new ArrayList<Configuration>();
	private final ConfigSource source;

	ConfigList(final ConfigSource source) {
		this(source, 10);
	}

	ConfigList(final ConfigSource source, final int initSize) {
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		if (initSize < 0) {
			throw new IllegalArgumentException("'initSize' must not be >= 0");
		}
		this.source = source;
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	@Override
	public int elementCount() {
		return this.entries.size();
	}

	@Override
	public Configuration get(final String key) {
		Configuration element = null;
		try {
			element = get(Integer.valueOf(key));
		} catch (final NumberFormatException e){
			// hickey-dee-doo!
		}
		return element;
	}

	@Override
	public String getString() {
		return this.entries.toString();
	}

	@Override
	public ConfigSource getSource() {
		return this.source;
	}

	@Override
	public Configuration get(final int index) {
		return this.entries.get(index);
	}

	@Override
	public int size() {
		return this.entries.size();
	}

	@Override
	public boolean validates() {
		return List.class.equals(this.type) && this.source != null && elementCount() > 0;
		//TODO immutability
	}

	boolean addConfiguration(final Configuration cfg) {
		return this.entries.add(cfg);
	}

	Configuration removeConfiguration(final int index) {
		checkIndex(index);
		return this.entries.remove(index);
	}

	Configuration getConfiguration(final int index) {
		checkIndex(index);
		return this.entries.get(index);
	}

	private void checkIndex(final int index) {
		if (index < 0) {
			throw new IllegalArgumentException("'index' must be >= 0");
		}
		if (index >= elementCount()) {
			throw new IndexOutOfBoundsException(String.format(
					"index out of bounds (%d): %d", size(), index));
		}
	}

	@Override
	public Integer getInt() {
		AbstractConfigElement.warnIneffectiveMethod();
		return null;
	}

	@Override
	public Float getFloat() {
		AbstractConfigElement.warnIneffectiveMethod();
		return null;
	}

	@Override
	public Boolean getBoolean() {
		AbstractConfigElement.warnIneffectiveMethod();
		return null;
	}
}
