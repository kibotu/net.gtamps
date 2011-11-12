package net.gtamps.shared.configuration;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ConfigMap extends AbstractMap<String, Configuration>
		implements Configuration {

	private static final long serialVersionUID = 7466530368525139233L;

	private final Class<?> type = Map.class;

	// TODO have the builder and whoever can change the set replace it with an
	// immutable version
	// or override all mutating methods of AbstractMap
	private final Set<Entry<String, Configuration>> entrySet = new HashSet<Entry<String, Configuration>>();

	private ConfigSource source;

	ConfigMap(ConfigSource source) {
		this.source = source;
	}

	@Override
	public int elementCount() {
		return entrySet.size();
	}

	@Override
	public Configuration get(String key) {
		String[] keyPair = AbstractConfigElement.splitKey(AbstractConfigElement
				.normalizeKey(key));
		if ("".equals(keyPair[1])) {
			return super.get(keyPair[0]);
		} else {
			return super.get(keyPair[0]).get(keyPair[1]);
		}
	}

	@Override
	public Configuration get(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index must be >= 0");
		}
		if (index >= entrySet.size()) {
			throw new IndexOutOfBoundsException(String.format(
					"index out of bounds (%d): %d", entrySet.size(), index));
		}
		Iterator<Entry<String, Configuration>> iter = entrySet.iterator();
		for (int i = 0; i < index; i++) {
			iter.next();
		}
		return iter.next().getValue();
	}

	@Override
	public String getString() {
		return entrySet.toString();
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
	public Class<?> getType() {
		return type;
	}

	@Override
	public ConfigSource getSource() {
		return source;
	}

	@Override
	public Set<Entry<String, Configuration>> entrySet() {
		return entrySet;
	}

	@Override
	public Configuration remove(Object key) {
		throw new UnsupportedOperationException(
				"this map does not support element removal by the public");
	}

	boolean validates() {
		return Map.class.equals(type) && source != null && elementCount() > 0;
		// TODO immutablity
		// TODO direct keys conform to "letter/underscore" rule for 1st
		// character
	}

}
