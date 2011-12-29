package net.gtamps.shared.serializer.communication.data;

public final class MapEntry<V extends AbstractSendableData<?>> extends AbstractSendableData<MapEntry<?>> {

	private static final long serialVersionUID = 3665372562982987754L;

	private String key = null;
	private V value = null;

	@SuppressWarnings("unchecked")
	public MapEntry() {
		super((Class<? extends AbstractSendableData<?>>) MapEntry.class);
	}

	public MapEntry<V> set(final String key, final V value) {
		setKey(key);
		setValue(value);
		return this;
	}

	public MapEntry<V> setKey(final String key) {
		if (key == null) {
			throw new IllegalArgumentException("'key' must not be 'null'");
		}
		this.key = key;
		return this;
	}

	public MapEntry<V> setValue(final V value) {
		this.value = value;
		return this;
	}

	public String key() {
		return key;
	}

	public V value() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MapEntry<?> other = (MapEntry<?>) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MapEntry [" + key + " -> " + (value == null ? "null" : value.toString()) + "]";
	}

	@Override
	protected void initHook() {
	}

	@Override
	protected void recycleHook() {
		if (value != null) {
			value.recycle();
		}
		key = null;
		value = null;
	}


}
