package net.gtamps.shared.configuration;

import java.util.AbstractMap;
import java.util.AbstractSet;
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
	
	private boolean isRoot;
	private ConfigSource source;

	ConfigMap(ConfigSource source) {
		this(source, false);
	}

	
	ConfigMap(ConfigSource source, boolean isRoot) {
		this.source = source;
		this.isRoot = isRoot;
	}

	@Override
	public int elementCount() {
		return entries.size();
	}

	@Override
	public Configuration get(String key) {
		String[] keyPair = ConfigurationBuilder.splitAndNormalizeKey(key);
		if ("".equals(keyPair[1])) {
			return entries.get(keyPair[0]);
		} else {
			return entries.get(keyPair[0]).get(keyPair[1]);
		}
	}

	@Override
	public Configuration get(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index must be >= 0");
		}
		if (index >= elementCount()) {
			throw new IndexOutOfBoundsException(String.format(
					"index out of bounds (%d): %d", elementCount(), index));
		}
		Iterator<String> iter = entries.keySet().iterator();
		for (int i = 0; i < index; i++) {
			iter.next();
		}
		return entries.get(iter.next());
	}

	@Override
	public String getString() {
		return entries.toString();
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
		return Collections.unmodifiableSet(entries.entrySet());
	}

	@Override
	public Configuration remove(Object key) {
		throw new UnsupportedOperationException(
				"this map does not support element removal by the public");
	}
	
	Configuration putConfiguration(String normKey, Configuration value) {
		return entries.put(normKey, value);
	}
	
	Configuration removeConfiguration(String normKey) {
		return entries.remove(normKey);
	}
	
	Configuration getConfiguration(String normKey) {
		return entries.get(normKey);
	}
	

	boolean validates() {
		return Map.class.equals(type) && source != null && elementCount() > 0;
		// TODO immutablity
		// TODO direct keys conform to "letter/underscore" rule for 1st
		// character
	}
	
	private class ArraySet<T> extends AbstractSet<T> {

		@Override
		public Iterator<T> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}
