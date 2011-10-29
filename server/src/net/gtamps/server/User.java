package net.gtamps.server;

/**
 * immutable!
 */
public final class User {
	
	public final int uid;
	public final String name;
	public transient final String password;

	public User(final int id, final String name) {
		if (name == null) {
			throw new IllegalArgumentException("'name' must not be null");
		}
		if (!isValidId(id)) {
			throw new IllegalArgumentException("'id' is invalid");
		}
		uid = id;
		this.name = name;
		password = null;
	}
	
	private boolean isValidId(final int id) {
		return id > 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uid;
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
		if (!(obj instanceof User)) {
			return false;
		}
		final User other = (User) obj;
		if (uid != other.uid) {
			return false;
		}
		return true;
	}
	
	
	
}
