package net.gtamps.shared.configuration;

public abstract class AbstractConfigSource implements Cloneable {

	protected final String name;
	protected final Class<?> type;
	private transient Integer hash = null;

	AbstractConfigSource(final String name, final Class<?> type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.getCanonicalName().hashCode());
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
		final ConfigSource other = (ConfigSource) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public abstract Object clone();

}