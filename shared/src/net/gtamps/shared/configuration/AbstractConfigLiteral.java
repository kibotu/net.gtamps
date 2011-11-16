package net.gtamps.shared.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

abstract class AbstractConfigLiteral implements Configuration {

	private static final long serialVersionUID = -1240349053596166316L;

	protected final ConfigSource source;
	protected final Object value;

	AbstractConfigLiteral(final Object value, final ConfigSource source) {
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		this.value = value;
		this.source = source;
	}

	@Override
	public abstract AbstractConfigLiteral clone();

	@Override
	public Class<?> getType() {
		return this.value.getClass();
	}

	@Override
	public ConfigSource getSource() {
		return this.source;
	}

	@Override
	public Collection<String> getKeys() {
		return Collections.singleton("0"); 
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Configuration select(final String key) {
		if (!"0".equals(key)) {
			return null;
		}
		return this;
	}

	@Override
	public Configuration select(final int index) {
		if (index != 0) {
			return null;
		}
		return this;
	}

	@Override
	public String getString() {
		return value == null ? "" : value.toString();
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

	@Override
	public String toString() {
		return getString();
	}

	@Override
	public Iterator<Configuration> iterator() {
		return Collections.singleton((Configuration) this).iterator();
	}


}
