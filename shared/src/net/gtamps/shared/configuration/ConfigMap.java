package net.gtamps.shared.configuration;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ConfigMap extends AbstractMap<String, Configuration>
implements Configuration {

	private static final long serialVersionUID = 7466530368525139233L;

	private final Class<?> type = Map.class;
	private final Map<String, Configuration> entries = new HashMap<String, Configuration>();

	private final ConfigSource source;

	ConfigMap(final ConfigSource source) {
		this.source = source;
	}

	@Override
	public int elementCount() {
		return this.entries.size();
	}

	@Override
	public Configuration get(final String key) {
		final ConfigKey configKey = new ConfigKey(key);
		if (configKey.isIntermediate()) {
			return this.entries.get(configKey.head).get(configKey.tail);
		} else {
			return this.entries.get(configKey.head);
		}
	}

	@Override
	public Configuration get(final int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index must be >= 0");
		}
		if (index >= elementCount()) {
			throw new IndexOutOfBoundsException(String.format(
					"index out of bounds (%d): %d", elementCount(), index));
		}
		final Iterator<String> iter = this.entries.keySet().iterator();
		for (int i = 0; i < index; i++) {
			iter.next();
		}
		return this.entries.get(iter.next());
	}

	@Override
	public Configuration get(final Object key) {
		throw new IllegalArgumentException("class mismatch: ConfigMap.get(...) takes only String oder int arguments, not " + key.getClass().getSimpleName());
	}

	@Override
	public String getString() {
		return this.entries.toString();
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
	public Set<Entry<String, Configuration>> entrySet() {
		return Collections.unmodifiableSet(this.entries.entrySet());
	}

	@Override
	public boolean validates() {
		return Map.class.equals(this.type) && this.source != null && elementCount() > 0;
		// TODO direct keys conform to "letter/underscore" rule for 1st
		// character
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


	Configuration putConfiguration(final String normKey, final Configuration value) {
		return this.entries.put(normKey, value);
	}

	Configuration removeConfiguration(final String normKey) {
		return this.entries.remove(normKey);
	}

	Configuration getConfiguration(final String normKey) {
		return this.entries.get(normKey);
	}

	void clearMap() {
		this.entries.clear();
	}

}
