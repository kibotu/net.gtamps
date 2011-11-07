package net.gtamps.shared.serializer.communication.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.gtamps.shared.SharedObject;

public class SharedMap<K extends Object, V extends Object> extends SharedObject implements
		Map<K, V> {
	
	private final Map<K, V> map;

	public SharedMap() throws ClassCastException {
		map = new HashMap<K,V>();
	}
	
	public SharedMap(int initialCapacity) throws ClassCastException {
		map = new HashMap<K,V>(initialCapacity);
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public V get(Object key) {
		return map.get(key);
	}

	public V put(K key, V value) {
		return map.put(key, value);
	}

	public V remove(Object key) {
		return map.remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	public void clear() {
		map.clear();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<V> values() {
		return map.values();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object o) {
		return map.equals(o);
	}

	public int hashCode() {
		return map.hashCode();
	}

}
