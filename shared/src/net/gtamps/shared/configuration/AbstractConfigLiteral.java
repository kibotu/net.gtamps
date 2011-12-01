package net.gtamps.shared.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * A literal value in a configuration, as opposed to a composite type like
 * list or map.
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 * @param <T> String, primitive or a primitive wrapper class
 */
abstract class AbstractConfigLiteral<T> implements Configuration {

	private static final long serialVersionUID = -1240349053596166316L;

	protected final AbstractConfigSource source;
	protected final Class<?> type;
	protected final T value;

	private transient Integer hash = null;

	AbstractConfigLiteral(final T value, final AbstractConfigSource source) {
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		validateValue(value);
		this.value = value;
		this.source = source;
		this.type = value == null ? Object.class : value.getClass();
	}

	@Override
	public abstract AbstractConfigLiteral<T> clone();

	@Override
	public AbstractConfigSource getSource() {
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
			keyDoesntExist(key);
		}
		return this;
	}

	@Override
	public Configuration select(final int index) {
		if (index != 0) {
			keyDoesntExist(Integer.toString(index));
		}
		return this;
	}

	@Override
	public String getString() {
		return value == null ? "" : value.toString();
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
	public String toString() {
		return getString();
	}

	@Override
	public Iterator<Configuration> iterator() {
		return Collections.singleton((Configuration) this).iterator();
	}

	@Override
	public int hashCode() {
		if (hash == null) {
			hash = hash();
		}
		return hash;
	}

	private int hash() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.getCanonicalName().hashCode());
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
		@SuppressWarnings("rawtypes")
		final AbstractConfigLiteral other = (AbstractConfigLiteral) obj;
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!(type == other.type)) {
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

	protected void keyDoesntExist(final String key) throws IllegalArgumentException {
		throw new IllegalArgumentException("key does not exist: " + key);
	}

	protected final void validateValue(final T value) throws IllegalArgumentException {
		if (value == null) {
			return;
		}
		final Class<?> type = value.getClass();
		if (!(String.class == type			// ordered by expected frequency
				|| Integer.class == type
				|| Boolean.class == type 
				|| Float.class == type 
				|| type.isPrimitive() 
				|| Long.class == type 
				|| Double.class == type 
				|| Byte.class == type 
				|| Character.class == type 
				|| Short.class == type 
		)) {
			throw new IllegalArgumentException("type not allowed in literal: " + type);
		}
	}
}
