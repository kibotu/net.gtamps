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
	final static Class<?> TYPE = java.util.Map.class;

	Map<String, Configuration> entries = new HashMap<String, Configuration>();

	private final AbstractConfigSource source;

	ConfigMap(final AbstractConfigSource source) {
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
		Configuration config = null;
		if (configKey.isIntermediate()) {
			config = this.entries.get(configKey.head).select(configKey.tail);
		} else {
			config = this.entries.get(configKey.head);
		}
		if (config == null) {
			throw new IllegalArgumentException("key does not exist: " + key);
		}
		return config;
	}

	@Override
	public Configuration select(final int index) {
		if (index < 0) {
			throw new IllegalArgumentException("key does not exist: index must be >= 0, is " + index);
		}
		if (index >= getCount()) {
			throw new IllegalArgumentException("key does not exist: " + index, new IndexOutOfBoundsException(String.format(
					"index out of bounds (%d): %d", getCount(), index)));
		}
		final Iterator<String> iter = this.entries.keySet().iterator();
		for (int i = 0; i < index; i++) {
			iter.next();
		}
		return this.entries.get(iter.next());
	}

	@Override
	public Configuration get(final Object key) {
		if (java.lang.String.class == key.getClass()) {
			return select((String) key);
		}
		throw new IllegalArgumentException("class mismatch: ConfigMap.get(...) takes only String arguments, not " + key.getClass().getSimpleName());
	}

	@Override
	public String getString() {
		return this.entries.toString();
	}

	@Override
	public Class<?> getType() {
		return TYPE;
	}

	@Override
	public AbstractConfigSource getSource() {
		return this.source;
	}

	@Override
	public Set<Entry<String, Configuration>> entrySet() {
		return Collections.unmodifiableSet(this.entries.entrySet());
	}

	@Override
	public Integer getInt() {
		throw new IllegalArgumentException("no int value");
	}


	@Override
	public Float getFloat() {
		throw new IllegalArgumentException("no float value");
	}


	@Override
	public Boolean getBoolean() {
		throw new IllegalArgumentException("no boolean value");
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
			cloneMap.entries.put(entry.getKey(), (Configuration) entry.getValue().clone());
		}
		return cloneMap;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (this.getClass() != o.getClass()) {
			return false;
		}
		return entries.equals(((ConfigMap) o).entries);
	}

	@Override
	public int hashCode() {
		return entries.hashCode();
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