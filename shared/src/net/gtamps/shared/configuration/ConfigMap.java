package net.gtamps.shared.configuration;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ConfigMap extends AbstractMap<String, Configuration>
implements Configuration {

	private static final long serialVersionUID = 7466530368525139233L;

	private final Class<?> type = java.util.Map.class;
	Map<String, Configuration> entries = new HashMap<String, Configuration>();

	private final ConfigSource source;

	ConfigMap(final ConfigSource source) {
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		this.source = source;
	}

	@Override
	public int getCount() {
		return this.entries.size();
	}

	@Override
	public Configuration select(final String key) {
		final ConfigKey configKey = new ConfigKey(key);
		if (configKey.isIntermediate()) {
			return this.entries.get(configKey.head).select(configKey.tail);
		} else {
			return this.entries.get(configKey.head);
		}
	}

	@Override
	public Configuration select(final int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index must be >= 0");
		}
		if (index >= getCount()) {
			throw new IndexOutOfBoundsException(String.format(
					"index out of bounds (%d): %d", getCount(), index));
		}
		final Iterator<String> iter = this.entries.keySet().iterator();
		for (int i = 0; i < index; i++) {
			iter.next();
		}
		return this.entries.get(iter.next());
	}

	@Override
	public Configuration get(final Object key) {
		throw new UnsupportedOperationException("class mismatch: ConfigMap.get(...) takes only String oder int arguments, not " + key.getClass().getSimpleName());
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
	public Integer getInt() {
		//TODO warn
		return null;
	}


	@Override
	public Float getFloat() {
		//TODO warn
		return null;
	}


	@Override
	public Boolean getBoolean() {
		//TODO warn
		return null;
	}

	@Override
	public Collection<String> getKeys() {
		return entries.keySet();
	}

	@Override
	public Iterator<Configuration> iterator() {
		return Collections.unmodifiableCollection(entries.values()).iterator();
	}

	@Override
	public ConfigMap clone() {
		final ConfigMap cloneMap = new ConfigMap(source);
		for (final Entry<String, Configuration> entry : entries.entrySet()) {
			cloneMap.entries.put(entry.getKey(), entry.getValue().clone());
		}
		return cloneMap;
	}

	Configuration putConfiguration(final String key, final Configuration value) {
		return entries.put(normalizeKey(key).head, value);
	}

	Configuration removeConfiguration(final String key) {
		return this.entries.remove(normalizeKey(key).head);
	}

	Configuration getConfiguration(final String key) {
		return this.entries.get(normalizeKey(key).head);
	}

	void clearMap() {
		this.entries.clear();
	}

	private ConfigKey normalizeKey(final String key) {
		final ConfigKey ckey = new ConfigKey(key);
		if (ckey.isIntermediate()) {
			throw new IllegalArgumentException("deep keys are not supported at this time");
		}
		return ckey;
	}


}